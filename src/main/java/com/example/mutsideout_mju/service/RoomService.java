package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.PaginationDto;
import com.example.mutsideout_mju.dto.request.room.CreateRoomDto;
import com.example.mutsideout_mju.dto.request.room.UpdateRoomDto;
import com.example.mutsideout_mju.dto.response.room.RoomListResponseData;
import com.example.mutsideout_mju.dto.response.room.RoomResponseData;
import com.example.mutsideout_mju.entity.Room;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.RoomRepository;
import com.example.mutsideout_mju.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    //집중 세션 방 전체 목록 조회
    public RoomListResponseData getRoomList(User user, PaginationDto paginationDto){
        //요청받은 페이지 번호, 페이지 크기, 작성순으로 정렬
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getPAGE_SIZE(), Sort.by(Sort.Order.desc("createdAt")));

        Page<Room> roomPage = roomRepository.findAll(pageable);  //해당 페이지 데이터를 모두 가져옴

        if(roomPage.getTotalPages() <= paginationDto.getPage() && paginationDto.getPage()!=0){
            throw new NotFoundException(ErrorCode.NOT_FOUND_PAGE);
        }

        return RoomListResponseData.roomListResponseData(roomPage);
    }

    //집중 세션 방 상세 조회
    public RoomResponseData getRoomById(UUID roomId){
        Room room = findRoomById(roomId);
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
