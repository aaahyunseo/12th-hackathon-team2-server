package com.example.mutsideout_mju.authentication;

import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class AccessTokenProvider {

    private final SecretKey key; // AccessToken secret 키
    private final long validityInMilliseconds; // AccessToken 유효 시간

    public AccessTokenProvider(@Value("${security.jwt.token.secret-access-key}") final String secretKey,
                               @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliseconds = validityInMilliseconds;
    }

    // 토큰 생성
    public String createToken(final String payload) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 정보 추출
    public String getPayload(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_TOKEN, e.getMessage());
        }
    }
}