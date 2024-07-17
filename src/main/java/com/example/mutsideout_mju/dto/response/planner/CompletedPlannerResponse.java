package com.example.mutsideout_mju.dto.response.planner;

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
    private final LocalDateTime modifiedDate;
}
