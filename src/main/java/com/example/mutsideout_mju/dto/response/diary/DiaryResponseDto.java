package com.example.mutsideout_mju.dto.response.diary;

import com.example.mutsideout_mju.entity.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class DiaryResponseDto {
    //감정일기 전체 목록 조회 시 사용되는 dto
    private UUID id;
    private String title;
    private String createdAt;

    public static DiaryResponseDto diaryResponseDto(Diary diary){
        return DiaryResponseDto.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
