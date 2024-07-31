package com.example.mutsideout_mju.dto.response.diary;

import com.example.mutsideout_mju.entity.Diary;
import com.example.mutsideout_mju.entity.ImageFile;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class DiaryResponseData {
    private UUID id;
    private String title;
    private String content;
    private List<String> images;
    private String createdAt;

    public static DiaryResponseData from(Diary diary) {
        List<String> images = diary.getImageFiles().stream()
                .map(ImageFile::getImageUrl)
                .collect(Collectors.toList());

        return DiaryResponseData.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .images(images)
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
