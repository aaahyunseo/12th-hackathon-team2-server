package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.survey.SurveyResultListDto;
import com.example.mutsideout_mju.dto.response.survey.SurveyQuestionData;
import com.example.mutsideout_mju.dto.response.survey.SurveyQuestionListResponseDto;
import com.example.mutsideout_mju.entity.*;
import com.example.mutsideout_mju.exception.ConflictException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.SurveyRepository;
import com.example.mutsideout_mju.repository.usersurvey.UserSurveyRepository;
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

    /**
     * 설문조사 질문 전체 조회
     */
    public SurveyQuestionListResponseDto getAllSurveyQuestions() {
        List<SurveyQuestionData> surveyQuestionDataList = surveyRepository.findAll().stream()
                .map(survey -> SurveyQuestionData.from(survey))
                .sorted(Comparator.comparing(SurveyQuestionData::getNumber))
                .collect(Collectors.toList());
        return SurveyQuestionListResponseDto.wrap(surveyQuestionDataList);
    }

    /**
     * 설문조사 응답 저장
     */
    @Transactional
    public void saveSurveyResults(User user, SurveyResultListDto surveyResultListDto) {
        // 설문에 이미 응답했는지 검증
        if (userSurveyRepository.existsByUserId(user.getId())) {
            throw new ConflictException(ErrorCode.SURVEY_ALREADY_PARTICIPATED);
        }

        // 설문 ID들 추출
        List<UUID> surveyIds = surveyResultListDto.getSurveyResultList().stream()
                .map(result -> UUID.fromString(result.getSurveyId()))
                .collect(Collectors.toList());

        // 해당 사용자가 제출한 설문 중복 검사
        List<UserSurvey> existingSurveys = userSurveyRepository.findByUserIdAndSurveyIdIn(user.getId(), surveyIds);
        if (!existingSurveys.isEmpty()) {
            throw new ConflictException(ErrorCode.DUPLICATED_SURVEY_ID);
        }

        // 설문 결과 저장
        List<UserSurvey> userSurveys = surveyResultListDto.getSurveyResultList()
                .stream()
                .map(surveyResult -> {
                    Survey survey = findExistingSurvey(UUID.fromString(surveyResult.getSurveyId()));
                    return UserSurvey.builder()
                            .user(user)
                            .survey(survey)
                            .surveyOption(surveyResult.getOption())
                            .build();
                })
                .collect(Collectors.toList());
        userSurveyRepository.saveAll(userSurveys);
    }

    private Survey findExistingSurvey(UUID surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SURVEY_NOT_FOUND));
    }
}
