package com.example.mutsideout_mju.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PaginationDto {
    private int totalPage;
    private int currentPage;

    public static <T> PaginationDto paginationDto(Page<T> page){
        return  PaginationDto.builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
    }
}