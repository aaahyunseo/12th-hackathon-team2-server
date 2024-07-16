package com.example.mutsideout_mju.authentication;

import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@AllArgsConstructor
public class JwtEncoder {
    public static final String TOKEN_TYPE = "Bearer ";

    public static String encodeJwtBearerToken(String token) {
        String cookieValue = TOKEN_TYPE + token;
        return URLEncoder.encode(cookieValue, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    public static String decodeJwtBearerToken(String cookieValue) {
        String value = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
        if (value.startsWith(TOKEN_TYPE)) {
            return value.substring(TOKEN_TYPE.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "token decode 실패");
    }
}
