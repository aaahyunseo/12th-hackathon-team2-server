package com.example.mutsideout_mju.dto.response.planner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class CompletedPlannerResponseForClient {
    private final UUID plannerId;
    private final String content;
    private final boolean isCompleted;
    private final String modifiedDate;
    private final UUID userId;

}

