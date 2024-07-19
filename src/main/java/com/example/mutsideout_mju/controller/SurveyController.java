package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.request.survey.SurveyResultListDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.survey.SurveyQuestionListResponseDto;
import com.example.mutsideout_mju.dto.response.user.UserGradeResponseDto;
import com.example.mutsideout_mju.entity.Grade;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.SurveyService;
import com.example.mutsideout_mju.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto<UserGradeResponseDto>> saveSurveyResults(@AuthenticatedUser User user,
                                                               @RequestBody @Valid SurveyResultListDto surveyResultListDto) {
        surveyService.saveSurveyResults(user, surveyResultListDto);
        UserGradeResponseDto userGradeResponseDto = userService.calculateUserGrade(user);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "설문조사 저장 및 유저 등급 반환 완료", userGradeResponseDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<SurveyQuestionListResponseDto>> getAllSurveyQuestions() {
        SurveyQuestionListResponseDto surveyList = surveyService.getAllSurveyQuestions();
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "설문조사 질문 전체 조회 완료", surveyList), HttpStatus.OK);
    }
}