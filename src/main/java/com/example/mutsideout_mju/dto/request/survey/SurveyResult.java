package com.example.mutsideout_mju.dto.request.survey;

import com.example.mutsideout_mju.entity.SurveyOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurveyResult {
    private String surveyId;
    private SurveyOption option;
}