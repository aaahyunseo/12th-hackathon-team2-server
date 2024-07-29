package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.Grade;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

@Getter
@Builder
public class ProfileResponseData {
    private String email;
    private String name;
    private Grade grade;

    public static ProfileResponseData of(String email, String name, Grade grade) {
        return builder()
                .email(email)
                .grade(grade)
                .name(name)
                .build();
    }
}
