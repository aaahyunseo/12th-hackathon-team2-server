package com.example.mutsideout_mju.dto.response.diary;

import com.example.mutsideout_mju.dto.response.PaginationData;
import com.example.mutsideout_mju.entity.Diary;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class DiaryListResponseData {
    private List<DiaryResponseDto> diaryList;
    private PaginationData pagination;

    public static DiaryListResponseData from(Page<Diary> page){
        return DiaryListResponseData.builder()
                .diaryList(page.stream()
                        //DiaryDto 형식으로 변환
                        .map(diary -> DiaryResponseDto.fromDiary(diary))
                        .collect(Collectors.toList()))
                .pagination(PaginationData.paginationData(page))
                .build();
    }
}
