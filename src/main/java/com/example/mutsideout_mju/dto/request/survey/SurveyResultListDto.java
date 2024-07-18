package com.example.mutsideout_mju.dto.request.survey;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class SurveyResultListDto {
    @NotBlank(message = "설문조사 답변이 누락되었습니다.")
    private List<SurveyResult> surveyResultList;

    public SurveyResultListDto(List<SurveyResult> resultList) {
        this.surveyResultList = resultList;
    }
}
