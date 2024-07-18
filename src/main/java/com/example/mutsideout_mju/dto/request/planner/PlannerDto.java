package com.example.mutsideout_mju.dto.planner;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(onConstructor_={@JsonCreator})
public class PlannerDto {
    @NotBlank(message = "내용을 입력해 주세요")
    String content;
}
