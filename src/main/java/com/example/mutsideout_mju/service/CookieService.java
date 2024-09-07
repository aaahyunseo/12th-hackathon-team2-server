package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.authentication.AuthenticationExtractor;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
@Service
public class CookieService {
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
}
