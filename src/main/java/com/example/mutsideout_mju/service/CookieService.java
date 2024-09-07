package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.authentication.AuthenticationExtractor;
import com.example.mutsideout_mju.authentication.RefreshTokenProvider;
import com.example.mutsideout_mju.entity.RefreshToken;
import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
@Service
@AllArgsConstructor
public class CookieService {
    private final RefreshTokenProvider refreshTokenProvider;
    public void setCookie(HttpServletResponse response, String accessToken) {
        ResponseCookie cookie = ResponseCookie.from(AuthenticationExtractor.TOKEN_COOKIE_NAME, accessToken)
                .maxAge(Duration.ofMillis(1800000))
                .path("/")
                .httpOnly(true)
                .sameSite("None").secure(true)
                .build();

        response.addHeader("set-cookie", cookie.toString());
    }

    public void setCookieForRefreshToken(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie_refresh = ResponseCookie.from("RefreshToken", refreshToken)
                .maxAge(Duration.ofDays(14))
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();

        response.addHeader("set-cookie", cookie_refresh.toString());
    }

    public void validateRefreshToken(RefreshToken refreshToken) {
        if (refreshTokenProvider.isTokenExpired(refreshToken.getToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
