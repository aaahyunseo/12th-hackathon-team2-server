package com.example.mutsideout_mju.dto.response.diary;

import com.example.mutsideout_mju.entity.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class DiaryResponseData {
    private UUID id;
    private String title;
    private String content;
    private String createdAt;

    public static DiaryResponseData diaryResponseData(Diary diary){
        return DiaryResponseData.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("YYYY-MM-DD")))
                .build();
    }
}
