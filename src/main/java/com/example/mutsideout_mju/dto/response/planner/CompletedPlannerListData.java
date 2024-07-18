package com.example.mutsideout_mju.dto.response.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Getter
@AllArgsConstructor
@Builder
public class CompletedPlannerListData {
    private Map<LocalDate, List<CompletedPlannerResponse>> data;
}
