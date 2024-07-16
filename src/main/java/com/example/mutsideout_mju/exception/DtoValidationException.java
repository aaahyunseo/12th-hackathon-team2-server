package com.example.mutsideout_mju.exception;

import com.example.mutsideout_mju.exception.errorCode.ErrorCode;

public class DtoValidationException extends CustomException {
    public DtoValidationException(ErrorCode errorCode, String detail) {super(errorCode, detail);}
}
