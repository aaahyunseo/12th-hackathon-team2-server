package com.example.mutsideout_mju.dto.request.survey;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SurveyResultListDto {
    @NotEmpty(message = "설문조사 답변이 누락되었습니다.")
    private List<SurveyResult> surveyResultList;
}
