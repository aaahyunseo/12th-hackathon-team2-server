package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.authentication.JwtTokenProvider;
import com.example.mutsideout_mju.authentication.PasswordHashEncryption;
import com.example.mutsideout_mju.authentication.RefreshTokenProvider;
import com.example.mutsideout_mju.dto.request.auth.LoginDto;
import com.example.mutsideout_mju.dto.request.auth.SignupDto;
import com.example.mutsideout_mju.dto.response.token.TokenResponseDto;
import com.example.mutsideout_mju.entity.RefreshToken;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.RefreshTokenRepository;
import com.example.mutsideout_mju.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordHashEncryption passwordHashEncryption;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RefreshTokenProvider refreshTokenProvider;

    /**
     * 회원가입
     */
    public TokenResponseDto signup(SignupDto signupDto) {

        // 중복 이름 회원가입 방지
        userService.validateIsDuplicatedName(signupDto.getName());

        // 중복 이메일 회원가입 방지
        userService.validateIsDuplicatedEmail(signupDto.getEmail());

        // 비밀번호 암호화
        String plainPassword = signupDto.getPassword();
        String hashedPassword = passwordHashEncryption.encrypt(plainPassword);

        User newUser = User.builder()
                .email(signupDto.getEmail())
                .password(hashedPassword)
                .name(signupDto.getName())
                .build();
        userRepository.save(newUser);

        return createToken(newUser);
    }

    /**
     * 로그인
     */
    public TokenResponseDto login(LoginDto loginDto) {
        // 유저 검증
        User user = findExistingUserByEmail(loginDto.getEmail());

        // 비밀번호 검증
        userService.validateIsPasswordMatches(loginDto.getPassword(), user.getPassword());

        // 토큰 생성
        return createToken(user);
    }

    /**
     * accessToken, refreshToken 재발급
     */
    public TokenResponseDto refresh(String refreshToken) {
        User user = validateRefreshToken(refreshToken);
        return createToken(user);
    }

    /**
     * 유저 refreshToken 관리
     */
    private User validateRefreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(refreshToken);

        // 저장된 refreshToken이 없는 경우
        if (storedRefreshToken == null) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN, "  저장된 RefreshToken이 null 입니다. ");
        }

        // 저장된 refreshToken이 만료된 경우
        if (refreshTokenProvider.isTokenExpired(storedRefreshToken.getToken())) {
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN, "토큰이 만료됐습니다.");
        }

        return userRepository.findById(storedRefreshToken.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * accessToken 토큰 생성 및 refreshToken 저장
     */
    private TokenResponseDto createToken(User user) {
        String payload = String.valueOf(user.getId());
        String accessToken = jwtTokenProvider.createToken(payload);
        // refreshToken 생성.
        String refreshTokenValue = refreshTokenProvider.createRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElse(new RefreshToken(user.getId(), refreshTokenValue));

        // refreshToken db에 저장.
        refreshToken.setToken(refreshTokenValue);
        refreshTokenRepository.save(refreshToken);

        return new TokenResponseDto(accessToken, refreshTokenValue);
    }

    private User findExistingUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));
    }
}
