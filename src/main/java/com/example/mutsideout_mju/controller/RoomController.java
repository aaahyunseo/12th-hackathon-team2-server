package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.request.PaginationDto;
import com.example.mutsideout_mju.dto.request.room.CreateRoomDto;
import com.example.mutsideout_mju.dto.request.room.UpdateRoomDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.room.RoomListResponseData;
import com.example.mutsideout_mju.dto.response.room.RoomResponseData;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    //집중 세션 방 전체 목록 조회
    @GetMapping
    public ResponseEntity<ResponseDto<RoomListResponseData>> getDiaryList(@RequestParam(value = "page", defaultValue = "1") int page) {
        PaginationDto paginationDto = new PaginationDto(page);
        RoomListResponseData roomListResponseData = roomService.getRoomList(paginationDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "집중 세션 방 전체 목록 조회에 성공하였습니다.", roomListResponseData), HttpStatus.OK);
    }

    //집중 세션 방 상세 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ResponseDto<RoomResponseData>> getRoomById(@PathVariable UUID roomId) {
        RoomResponseData roomResponseData = roomService.getRoomById(roomId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "집중 세션 방 상세 조회에 성공하였습니다.", roomResponseData), HttpStatus.OK);
    }

    //집중 세션 방 생성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createRoom(@AuthenticatedUser User user, @RequestBody @Valid CreateRoomDto createRoomDto) {
        roomService.createRoom(user, createRoomDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "실시간 집중 세션 방이 정상적으로 생성되었습니다."), HttpStatus.CREATED);
    }

    //집중 세션 방 수정
    @PatchMapping("/{roomId}")
    public ResponseEntity<ResponseDto<Void>> updateRoomById(@AuthenticatedUser User user,
                                                            @PathVariable UUID roomId,
                                                            @RequestBody @Valid UpdateRoomDto updateRoomDto) {
        roomService.updateRoomById(user, roomId, updateRoomDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "실시간 집중 세션 방 정보가 정상적으로 수정되었습니다."), HttpStatus.OK);
    }

    //집중 세션 방 삭제
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ResponseDto<Void>> deleteRoomById(@AuthenticatedUser User user, @PathVariable UUID roomId) {
        roomService.deleteRoomById(user, roomId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "실시간 집중 세션 방이 정상적으로 삭제되었습니다."), HttpStatus.OK);
    }
}
