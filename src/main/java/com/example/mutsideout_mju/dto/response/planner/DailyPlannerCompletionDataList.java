package com.example.mutsideout_mju.dto.response.planner;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class DailyPlannerCompletionDataList {
    private List<DailyPlannerCompletionData> dailyList;

    public static DailyPlannerCompletionDataList from(Map<String, Long> dailyCounts) {
        List<DailyPlannerCompletionData> dailyList = dailyCounts.entrySet().stream()
                .map(entry -> DailyPlannerCompletionData.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return new DailyPlannerCompletionDataList(dailyList);
    }
}
