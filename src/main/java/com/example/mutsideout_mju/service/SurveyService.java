package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.survey.SurveyResult;
import com.example.mutsideout_mju.dto.request.survey.SurveyResultListDto;
import com.example.mutsideout_mju.dto.response.survey.SurveyQuestionData;
import com.example.mutsideout_mju.dto.response.survey.SurveyQuestionListResponseDto;
import com.example.mutsideout_mju.entity.*;
import com.example.mutsideout_mju.exception.ConflictException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.SurveyRepository;
import com.example.mutsideout_mju.repository.UserSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final UserSurveyRepository userSurveyRepository;

    public SurveyQuestionListResponseDto getAllSurveyQuestions() {
        List<SurveyQuestionData> surveyQuestionDataList = surveyRepository.findAll().stream()
                .map(survey -> SurveyQuestionData.from(survey))
                .sorted(Comparator.comparing(SurveyQuestionData::getNumber))
                .collect(Collectors.toList());
        return SurveyQuestionListResponseDto.wrap(surveyQuestionDataList);
    }

    @Transactional
    public void saveSurveyResults(User user, SurveyResultListDto surveyResultListDto) {
        // 설문에 이미 응답했는지 검증
        if (userSurveyRepository.existsByUserId(user.getId())) {
            throw new ConflictException(ErrorCode.SURVEY_ALREADY_PARTICIPATED);
        }
        surveyResultListDto.getSurveyResultList().forEach(surveyResult -> {
            // 같은 설문에 답변했는지 중복 필드 검증
            if (userSurveyRepository.existsByUserIdAndSurveyId(user.getId(), UUID.fromString(surveyResult.getSurveyId()))) {
                throw new ConflictException(ErrorCode.DUPLICATED_SURVEY_ID);
            }
            Survey survey = findExistingSurvey(UUID.fromString(surveyResult.getSurveyId()));
            UserSurvey userSurvey = UserSurvey.builder()
                    .user(user)
                    .survey(survey)
                    .surveyOption(surveyResult.getOption())
                    .build();
            userSurveyRepository.save(userSurvey);
        });
    }

    private Survey findExistingSurvey(UUID surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SURVEY_NOT_FOUND));
    }
}
