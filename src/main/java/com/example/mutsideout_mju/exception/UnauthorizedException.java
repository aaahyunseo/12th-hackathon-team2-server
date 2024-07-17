package com.example.mutsideout_mju.exception;

import com.example.mutsideout_mju.exception.errorCode.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
    public UnauthorizedException(ErrorCode errorCode) {super(errorCode);}
}
