package com.example.mutsideout_mju.dto.response.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class SurveyQuestionListResponseDto {
    private List<SurveyQuestionData> surveyQuestionDataList;

    public static SurveyQuestionListResponseDto wrap(List<SurveyQuestionData> surveyQuestionDataList) {
        return new SurveyQuestionListResponseDto(surveyQuestionDataList);
    }
}
