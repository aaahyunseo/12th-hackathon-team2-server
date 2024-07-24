package com.example.mutsideout_mju.dto.response.planner;

import com.example.mutsideout_mju.entity.Planner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static CompletedPlannerResponse fromPlanner(Planner planner) {
        return CompletedPlannerResponse.builder()
                .plannerId(planner.getId())
                .content(planner.getContent())
                .isCompleted(planner.isCompleted())
                .modifiedDate(planner.getModifiedDate())
                .userId(planner.getUser().getId())
                .build();
    }

    public CompletedPlannerResponseForClient toClientResponse() {
        return CompletedPlannerResponseForClient.builder()
                .plannerId(this.plannerId)
                .content(this.content)
                .modifiedDate(this.modifiedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .userId(this.userId)
                .isCompleted(this.isCompleted)
                .build();
    }
}
