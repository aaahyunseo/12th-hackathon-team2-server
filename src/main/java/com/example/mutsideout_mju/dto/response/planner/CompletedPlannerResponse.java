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
    private final LocalDateTime modifiedDate;
    private final UUID userId;

    public CompletedPlannerResponse(Planner planner) {
        this.plannerId = planner.getId();
        this.content = planner.getContent();
        this.isCompleted = planner.isCompleted();
        this.modifiedDate = planner.getModifiedDate();
        this.userId = planner.getUser().getId();
    }
}
