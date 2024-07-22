package com.example.mutsideout_mju.dto.response.planner;

import com.example.mutsideout_mju.entity.Planner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class PlannerResponseData {
    private final UUID plannerId;
    private final String content;
    private final boolean isCompleted;
    private final String createdAt;

    public static PlannerResponseData fromPlanner(Planner planner) {
        return new PlannerResponseData(
                planner.getId(),
                planner.getContent(),
                planner.isCompleted(),
                planner.getModifiedDate().toString()
        );
    }
}
