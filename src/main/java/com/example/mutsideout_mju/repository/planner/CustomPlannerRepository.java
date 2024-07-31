package com.example.mutsideout_mju.repository.planner;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface CustomPlannerRepository {
    Map<String, Long> getCompletedPlannersStats(UUID userId, LocalDate startDate, LocalDate endDate);
}
