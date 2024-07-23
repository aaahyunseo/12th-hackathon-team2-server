package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.user.UserGradeResponseDto;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/grade")
    public ResponseEntity<ResponseDto<UserGradeResponseDto>> getUserGrade(@AuthenticatedUser User user) {
        UserGradeResponseDto userGradeResponseDto = userService.getUserGrade(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "유저 등급 조회 완료", userGradeResponseDto), HttpStatus.OK);
    }
}
