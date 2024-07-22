package com.example.mutsideout_mju.dto.response.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PlannerListResponseData {
    private List<PlannerResponseData> plannerList;

}
