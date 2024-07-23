package com.example.mutsideout_mju.dto.request.room;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateRoomDto {
    @NotBlank(message = "제목이 누락되었습니다.")
    private String title;

    @NotBlank(message = "링크가 누락되었습니다.")
    private String link;

    @NotBlank(message = "상세 설명이 누락되었습니다.")
    private String content;
}
