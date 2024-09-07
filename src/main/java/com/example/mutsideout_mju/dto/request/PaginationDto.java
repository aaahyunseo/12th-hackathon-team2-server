package com.example.mutsideout_mju.dto.request;

import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class PaginationDto {
    private final int pageSize = 10;    //10개씩 페이징
    private int page;                    //요청받은 페이지

    public PaginationDto(int page) {
        if (pageSize <= page && page != 0) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PAGE);
        }
        this.page = Math.max(page - 1, 0);
    }
}
