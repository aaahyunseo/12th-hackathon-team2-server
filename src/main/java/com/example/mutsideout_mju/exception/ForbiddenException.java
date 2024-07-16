package com.example.mutsideout_mju.exception;

import com.example.mutsideout_mju.exception.errorCode.ErrorCode;

public class ForbiddenException extends CustomException {
    public ForbiddenException(ErrorCode errorCode) {super(errorCode);}
}
