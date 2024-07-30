package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.PaginationDto;
import com.example.mutsideout_mju.dto.request.room.CreateRoomDto;
import com.example.mutsideout_mju.dto.request.room.UpdateRoomDto;
import com.example.mutsideout_mju.dto.response.PaginationData;
import com.example.mutsideout_mju.dto.response.room.RoomListResponseData;
import com.example.mutsideout_mju.dto.response.room.RoomResponseData;
import com.example.mutsideout_mju.dto.response.room.RoomResponseDto;
import com.example.mutsideout_mju.entity.Room;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.RoomRepository;
import com.example.mutsideout_mju.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final S3Service s3Service;

    /**
     * 24시간 지난 방 삭제 (한 시간마다 실행)
     */
    @Scheduled(fixedRate = 3600000)
    public void deleteOldRooms() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        roomRepository.deleteByCreatedAtBefore(cutoffTime);
    }

    /**
     * 집중 세션 방 전체 목록 조회
     */
    public RoomListResponseData getRoomList(PaginationDto paginationDto) {
        // 요청받은 페이지 번호, 페이지 크기, 작성순으로 정렬
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getPAGE_SIZE(), Sort.by(Sort.Order.desc("createdAt")));

        // 해당 페이지 데이터를 모두 가져옴
        Page<Room> roomPage = roomRepository.findAll(pageable);
        if (roomPage.getTotalPages() <= paginationDto.getPage() && paginationDto.getPage() != 0) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PAGE);
        }

        List<RoomResponseDto> roomResponseList = roomPage.getContent().stream()
                .map(room -> RoomResponseDto.fromRoom(room, s3Service.getRoomImageLink(room.getLink())))
                .collect(Collectors.toList());

        PaginationData pagination = PaginationData.paginationData(roomPage);
        return new RoomListResponseData(roomResponseList, pagination);
    }

    /**
     * 집중 세션 방 상세 조회
     */
    public RoomResponseData getRoomById(UUID roomId) {
        Room room = findExistingRoom(roomId);
        //현재 시각으로 부터 방 생성 시간이 24시간 이후이면 false(비활성화)로 방 삭제
        if (!room.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24))) {
            roomRepository.deleteById(roomId);
            throw new NotFoundException(ErrorCode.ROOM_NOT_FOUND);
        }
        return RoomResponseData.from(room);
    }

    /**
     * 집중 세션 방 생성
     */
    public void createRoom(User user, CreateRoomDto createRoomDto) {
        Room room = Room.builder()
                .title(createRoomDto.getTitle())
                .link(createRoomDto.getLink())
                .content(createRoomDto.getContent())
                .user(user)
                .build();
        roomRepository.save(room);
    }

    /**
     * 집중 세션 방 수정
     */
    public void updateRoomById(User user, UUID roomId, UpdateRoomDto updateRoomDto) {
        Room updateRoom = findRoomByUserIdAndRoomId(user.getId(), roomId);
        updateRoom.setTitle(updateRoomDto.getTitle())
                .setLink(updateRoomDto.getLink())
                .setContent(updateRoomDto.getContent());
        roomRepository.save(updateRoom);
    }

    /**
     * 집중 세션 방 삭제
     */
    public void deleteRoomById(User user, UUID roomId) {
        Room room = findRoomByUserIdAndRoomId(user.getId(), roomId);
        roomRepository.delete(room);
    }

    private Room findRoomByUserIdAndRoomId(UUID userId, UUID roomId) {
        return roomRepository.findByUserIdAndId(userId, roomId)
                .orElseThrow(() -> new ForbiddenException(ErrorCode.NO_ACCESS));
    }

    private Room findExistingRoom(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
    }
}
