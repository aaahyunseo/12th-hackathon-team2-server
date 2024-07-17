package com.example.mutsideout_mju.dto.response.diary;

import com.example.mutsideout_mju.dto.response.PaginationDto;
import com.example.mutsideout_mju.entity.Diary;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class DiaryListResponseData {
    private List<DiaryResponseData> diaryList;
    private PaginationDto pagination;

    public static DiaryListResponseData diaryListResponseData(Page<Diary> page){
        return DiaryListResponseData.builder()
                .diaryList(page.stream()
                        //DiaryDto 형식으로 변환
                        .map(diary -> DiaryResponseData.diaryResponseData(diary))
                        .collect(Collectors.toList()))
                .pagination(PaginationDto.paginationDto(page))
                .build();
    }
}
