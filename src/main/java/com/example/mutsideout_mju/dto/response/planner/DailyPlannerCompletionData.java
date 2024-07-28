package com.example.mutsideout_mju.dto.response.planner;

import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.StatsGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyPlannerCompletionData {
    private String date;
    private StatsGrade statsGrade;

    public static DailyPlannerCompletionData of(String date, Long count) {
        StatsGrade statsGrade = Planner.determineStatsGrade(count);
        return new DailyPlannerCompletionData(date, statsGrade);
    }
}