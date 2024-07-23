package com.example.mutsideout_mju.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PaginationData {
    private int totalPage;
    private int currentPage;

    public static <T> PaginationData paginationData(Page<T> page) {
        return PaginationData.builder()
                .totalPage(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .build();
    }
}