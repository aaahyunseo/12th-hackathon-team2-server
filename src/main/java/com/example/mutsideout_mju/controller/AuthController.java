package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticationExtractor;
import com.example.mutsideout_mju.authentication.JwtEncoder;
import com.example.mutsideout_mju.dto.request.auth.LoginDto;
import com.example.mutsideout_mju.dto.request.auth.SignupDto;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.dto.response.token.TokenResponseDto;
import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.service.AuthService;
import com.example.mutsideout_mju.service.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;
    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<Void>> signup(@RequestBody @Valid SignupDto signupDto, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = authService.signup(signupDto);
        setCookie(response, JwtEncoder.encode(tokenResponseDto.getAccessToken()));
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED, "회원가입 완료"), HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = authService.login(loginDto);
        setCookie(response, JwtEncoder.encode(tokenResponseDto.getAccessToken()));
        setCookieForRefreshToken(response, tokenResponseDto.getRefreshToken());
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "로그인 완료"), HttpStatus.OK);
    }

    // RefreshToken 발급
    @GetMapping("/refresh")
    public ResponseEntity<ResponseDto<Void>> refresh(HttpServletResponse response, HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookie(request);
        TokenResponseDto tokenResponseDto;
        try {
            tokenResponseDto = authService.refresh(refreshToken);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(ResponseDto.res(HttpStatus.UNAUTHORIZED, "Refresh token 만료 혹은 부적절"), HttpStatus.UNAUTHORIZED);
        }
        setCookie(response, JwtEncoder.encode(tokenResponseDto.getAccessToken()));
        setCookieForRefreshToken(response, tokenResponseDto.getRefreshToken());

        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "Refresh token 재생성 완료"), HttpStatus.OK);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("RefreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new UnauthorizedException(ErrorCode.INVALID_TOKEN);
    }

    private void clearCookies(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from("AccessToken", null)
                .maxAge(0)
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("RefreshToken", null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(final HttpServletResponse response) {
        clearCookies(response);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK, "로그아웃 완료"), HttpStatus.OK);
    }
}
