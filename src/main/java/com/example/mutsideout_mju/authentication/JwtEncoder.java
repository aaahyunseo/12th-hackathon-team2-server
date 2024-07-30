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

    /**
     * 토큰에 Bearer 형식 적용 후 인코딩
     * 공백은 '+' 대신 '%20'으로 치환
     * @param token 인코딩할 JWT 토큰
     * @return 인코딩된 JWT 토큰
     */
    public static String encode(String token) {
        String cookieValue = TOKEN_TYPE + token;
        return URLEncoder.encode(cookieValue, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

    /**
     * Bearer 형식 적용된 쿠키 값 디코딩
     * @param cookieValue 디코딩할 쿠키 값
     * @return 디코딩된 JWT 토큰
     * @throws UnauthorizedException Bearer 형식이 적용된 값이 아닐 경우 발생
     */
    public static String decode(String cookieValue) {
        String value = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
        if (value.startsWith(TOKEN_TYPE)) {
            return value.substring(TOKEN_TYPE.length());
        }
        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, "token decode 실패");
    }
}
