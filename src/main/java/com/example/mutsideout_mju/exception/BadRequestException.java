package com.example.mutsideout_mju.exception;

import com.example.mutsideout_mju.exception.errorCode.ErrorCode;

public class BadRequestException extends CustomException {
    public BadRequestException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
