package com.example.mutsideout_mju.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //UnauthorizedException
    INVALID_PASSWORD("4010", "유효하지 않은 비밀번호입니다."),
    COOKIE_NOT_FOUND("4011", "쿠키를 찾을 수 없습니다."),
    INVALID_TOKEN("4012", "유효하지 않은 토큰입니다."),

    //ForbiddenException
    NO_ACCESS("4030", "접근 권한이 없습니다."),
    INVALID_PLANNER_ACCESS("4013", "플래너에 대해 유효하지 않는 상태 입니다."),

    //NotFoundException
    USER_NOT_FOUND("4040","존재하지 않는 사용자입니다."),
    NOT_FOUND_PAGE("4041","존재하지 않는 페이지입니다."),
    DIARY_NOT_FOUND("4042","존재하지 않는 일기입니다."),
    PLANNER_NOT_FOUND("4043", "존재하지 않는 플랜입니다."),
    SURVEY_NOT_FOUND("4044", "존재하지 않는 설문입니다."),

    //ConflictException
    DUPLICATED_NAME("4090","이미 사용 중인 이름입니다."),
    DUPLICATED_EMAIL("4091", "이미 사용 중인 이메일입니다."),
    DUPLICATED_SURVEY_ID("4092", "중복된 설문에 답하셨습니다."),
    SURVEY_ALREADY_PARTICIPATED("4093", "이미 설문조사에 참여하였습니다."),

    //ValidationException
    NOT_NULL("9001", "필수값이 누락되었습니다."),
    NOT_BLANK("9002", "필수값이 빈 값이거나 공백으로 되어있습니다."),
    REGEX("9003", "형식에 맞지 않습니다."),
    LENGTH("9004", "길이가 유효하지 않습니다.");

    private final String code;
    private final String message;

    public static ErrorCode resolveValidationErrorCode(String code) {
        return switch (code) {
            case "NotNull" -> NOT_NULL;
            case "NotBlank" -> NOT_BLANK;
            case "Pattern" -> REGEX;
            case "Length" -> LENGTH;
            default -> throw new IllegalArgumentException("Unexpected value: " + code);
        };
    }
}
