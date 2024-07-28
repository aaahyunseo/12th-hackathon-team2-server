package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.entity.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseData {
    private String email;
    private String name;
    private Grade grade;
}
