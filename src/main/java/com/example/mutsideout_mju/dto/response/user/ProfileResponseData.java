package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.UserGrade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseData {
    private String email;
    private String name;
    private UserGrade userGrade;

    public static ProfileResponseData of(String email, String name, UserGrade userGrade) {
        return builder()
                .email(email)
                .userGrade(userGrade)
                .name(name)
                .build();
    }
}
