package com.example.mutsideout_mju.dto.response.survey;

import com.example.mutsideout_mju.entity.Survey;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class SurveyQuestionData {
    private String surveyId;
    private Long number;
    private String question;

    public static SurveyQuestionData from(Survey survey) {
        return SurveyQuestionData.builder()
                .surveyId(String.valueOf(survey.getId()))
                .number(survey.getNumber())
                .question(survey.getQuestion())
                .build();
    }
}
