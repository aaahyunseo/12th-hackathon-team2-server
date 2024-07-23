package com.example.mutsideout_mju.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PaginationDto {
    private final int PAGE_SIZE = 10;    //10개씩 페이징
    private int page;                    //요청받은 페이지

    public PaginationDto(int page) {
        this.page = Math.max(page - 1, 0);
    }
}
