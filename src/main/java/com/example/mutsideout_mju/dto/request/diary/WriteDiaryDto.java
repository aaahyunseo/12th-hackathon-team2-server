package com.example.mutsideout_mju.dto.request.diary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WriteDiaryDto {
    @NotBlank(message = "감정일기 제목이 누락되었습니다.")
    @Size(max = 30)
    private String title;

    @NotBlank(message = "감정일기 내용이 누락되었습니다.")
    @Size(max = 200)
    private String content;
}
