package com.example.mutsideout_mju.dto.response.planner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class GroupedCompletedPlannerResponse {
    private Map<String, List<CompletedPlannerResponse>> groupedPlanners;
}