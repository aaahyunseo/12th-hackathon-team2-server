package com.example.mutsideout_mju.dto.response.planner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CompletedPlannerListResponseData {
    private List<CompletedPlannerResponse> completedPlanners;
}
