package com.example.mutsideout_mju.repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface CustomPlannerRepository {
    Map<String, Long> getCompletedPlannersStatistics(UUID userId, LocalDate startDate, LocalDate endDate);
}
