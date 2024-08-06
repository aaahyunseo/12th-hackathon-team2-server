package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.entity.UserGrade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponseDto {
    private String email;
    private String name;
    private UserGrade userGrade;

    public static UserInfoResponseDto of(String email, String name, UserGrade userGrade) {
        return builder()
                .email(email)
                .userGrade(userGrade)
                .name(name)
                .build();
    }

    public static UserInfoResponseDto of(String name, UserGrade userGrade) {
        return builder()
                .name(name)
                .userGrade(userGrade)
                .build();
    }

    public static UserInfoResponseDto from(UserGrade userGrade) {
        return builder()
                .userGrade(userGrade)
                .build();
    }
}
