package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.request.user.DeleteUserDto;
import com.example.mutsideout_mju.dto.request.user.UpdateUserDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.user.ProfileResponseData;
import com.example.mutsideout_mju.dto.response.user.UserGradeResponseDto;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 유저 등급 조회
    @GetMapping("/grade")
    public ResponseEntity<ResponseDto<UserGradeResponseDto>> getUserGrade(@AuthenticatedUser User user) {
        UserGradeResponseDto userGradeResponseDto = userService.getUserGrade(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 등급 조회 완료", userGradeResponseDto), HttpStatus.OK);
    }

    // 유저 정보 수정
    @PatchMapping
    public ResponseEntity<ResponseDto<UserGradeResponseDto>> updateUser(@AuthenticatedUser User user,
                                                                        @RequestBody @Valid UpdateUserDto updateUserDto) {
        userService.updateUser(user, updateUserDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 정보 수정 완료"), HttpStatus.OK);
    }

    // 유저 탈퇴
    @DeleteMapping
    public ResponseEntity<ResponseDto<UserGradeResponseDto>> deleteUser(@AuthenticatedUser User user,
                                                                        @RequestBody @Valid DeleteUserDto deleteUserDto) {
        userService.deleteUser(user, deleteUserDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "회원 탈퇴 성공"), HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<ResponseDto<ProfileResponseData>> getMyPage(@AuthenticatedUser User user) {
        ProfileResponseData profileResponseData = userService.getMyPage(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "회원 정보 조회 완료", profileResponseData), HttpStatus.OK);
    }
}
