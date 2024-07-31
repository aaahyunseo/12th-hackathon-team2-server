package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.UserGrade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGradeResponseDto {
    private String name;
    private UserGrade userGrade;

    public static UserGradeResponseDto of(String name, UserGrade userGrade) {
        return UserGradeResponseDto.builder()
                .name(name)
                .userGrade(userGrade)
                .build();
    }

    public static UserGradeResponseDto from(UserGrade userGrade) {
        return UserGradeResponseDto.builder()
                .userGrade(userGrade)
                .build();
    }
}
