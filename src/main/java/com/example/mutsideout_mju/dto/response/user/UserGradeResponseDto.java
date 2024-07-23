package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGradeResponseDto {
    private String name;
    private Grade grade;

    public static UserGradeResponseDto of(String name, Grade grade) {
        return UserGradeResponseDto.builder()
                .name(name)
                .grade(grade)
                .build();
    }

    public static UserGradeResponseDto from(Grade grade) {
        return UserGradeResponseDto.builder()
                .grade(grade)
                .build();
    }
}
