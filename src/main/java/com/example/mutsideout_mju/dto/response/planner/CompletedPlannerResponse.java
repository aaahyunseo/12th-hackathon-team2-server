package com.example.mutsideout_mju.dto.response.planner;

import com.example.mutsideout_mju.entity.Planner;
import com.example.mutsideout_mju.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CompletedPlannerResponse {
    private final UUID plannerId;
    private final String content;
    private final boolean isCompleted;
    private final String modifiedDate;
    private final UUID userId;

    public static CompletedPlannerResponse fromPlanner(Planner planner) {
        return new CompletedPlannerResponse(
                planner.getId(),
                planner.getContent(),
                planner.isCompleted(),
                planner.getModifiedDate().toString(),
                planner.getUser().getId()
        );
    }
}
