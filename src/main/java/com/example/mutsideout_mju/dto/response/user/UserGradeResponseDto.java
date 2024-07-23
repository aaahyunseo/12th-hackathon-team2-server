package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGradeResponseDto {
    private String name;
    private Grade grade;

    public static UserGradeResponseDto fromUserDetails(String name, Grade grade) {
        return UserGradeResponseDto.builder()
                .name(name)
                .grade(grade)
                .build();
    }

    public static UserGradeResponseDto fromGradeOnly(Grade grade) {
        return UserGradeResponseDto.builder()
                .grade(grade)
                .build();
    }
}
