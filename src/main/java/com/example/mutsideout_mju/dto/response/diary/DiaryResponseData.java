package com.example.mutsideout_mju.dto.response.diary;

import com.example.mutsideout_mju.dto.response.image.ImageData;
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
    private List<ImageData> imageDataList;
    private String createdAt;

    public static DiaryResponseData from(Diary diary) {
        List<ImageData> images = diary.getImageFiles().stream()
                .map(imageFile -> new ImageData(imageFile.getId(), imageFile.getImageUrl()))
                .toList();

        return DiaryResponseData.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .imageDataList(images)
                .createdAt(diary.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }
}
