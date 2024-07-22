package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.PaginationDto;
import com.example.mutsideout_mju.dto.request.room.CreateRoomDto;
import com.example.mutsideout_mju.dto.request.room.UpdateRoomDto;
import com.example.mutsideout_mju.dto.response.room.RoomListResponseData;
import com.example.mutsideout_mju.dto.response.room.RoomResponseData;
import com.example.mutsideout_mju.dto.response.room.RoomResponseDto;
import com.example.mutsideout_mju.entity.Room;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    //집중 세션 방 전체 목록 조회
    public RoomListResponseData getRoomList(PaginationDto paginationDto){
        //요청받은 페이지 번호, 페이지 크기, 작성순으로 정렬
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getPAGE_SIZE(), Sort.by(Sort.Order.desc("createdAt")));

        // 24시간이 지난 방 삭제
        List<Room> deactivatedRooms = roomRepository.findAll().stream()
                .filter(room -> room.getCreatedAt().isBefore(LocalDateTime.now().minusHours(24)))
                .collect(Collectors.toList());
        roomRepository.deleteAll(deactivatedRooms);

        // 비활성화 된 방 삭제 후 해당 페이지 데이터를 모두 가져옴
        Page<Room> roomPage = roomRepository.findAll(pageable);

        if(roomPage.getTotalPages() <= paginationDto.getPage() && paginationDto.getPage()!=0){
            throw new NotFoundException(ErrorCode.NOT_FOUND_PAGE);
        }

        return RoomListResponseData.roomListResponseData(roomPage);
    }

    //집중 세션 방 상세 조회
    public RoomResponseData getRoomById(UUID roomId){
        Room room = findRoomById(roomId);
        //현재 시각으로 부터 방 생성 시간이 24시간 이후이면 false(비활성화)로 방 삭제
        if(!room.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24))){
            roomRepository.deleteById(roomId);
            throw new NotFoundException(ErrorCode.ROOM_NOT_FOUND);
        }
        return RoomResponseData.roomResponseData(room);
    }

    //집중 세션 방 생성
    public void createRoom(User user, CreateRoomDto createRoomDto){
        Room room = Room.builder()
                .title(createRoomDto.getTitle())
                .link(createRoomDto.getLink())
                .content(createRoomDto.getContent())
                .user(user)
                .build();
        roomRepository.save(room);
    }

    //집중 세션 방 수정
    public void updateRoomById(User user, UUID roomId, UpdateRoomDto updateRoomDto){
        Room updateRoom = findRoomById(roomId);
        checkUser(user, updateRoom);
        updateRoom.setTitle(updateRoomDto.getTitle())
                .setLink(updateRoomDto.getLink())
                .setContent(updateRoomDto.getContent());
        roomRepository.save(updateRoom);
    }

    //집중 세션 방 삭제
    public void deleteRoomById(User user, UUID roomId){
        checkUser(user, findRoomById(roomId));
        roomRepository.deleteById(roomId);
    }

    //방 존재 여부 확인
    private Room findRoomById(UUID roomId){
        return roomRepository.findById(roomId).orElseThrow(()-> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
    }

    //접근 유저와 방 생성자가 일치한지 확인
    private void checkUser(User user, Room room){
        if(!room.getUser().getId().equals(user.getId())){
            throw new ForbiddenException(ErrorCode.NO_ACCESS);
        }
    }
}
