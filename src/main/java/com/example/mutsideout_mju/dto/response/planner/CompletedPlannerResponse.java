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
        return new CompletedPlannerResponse(
                planner.getId(),
                planner.getContent(),
                planner.isCompleted(),
                planner.getModifiedDate(),
                planner.getUser().getId()
        );
    }
    public String getFormattedDate() {
        return this.modifiedDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public CompletedPlannerResponseForClient toClientResponse() {
        return CompletedPlannerResponseForClient.builder()
                .plannerId(this.plannerId)
                .content(this.content)
                .isCompleted(this.isCompleted)
                .formattedDate(this.modifiedDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .userId(this.userId)
                .build();
    }
}
