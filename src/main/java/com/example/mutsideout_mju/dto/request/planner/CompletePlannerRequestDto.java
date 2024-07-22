package com.example.mutsideout_mju.dto.request.planner;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletePlannerRequestDto {
    @NotNull(message = "완료 여부를 기입해 주세요")
    private Boolean isCompleted;
}