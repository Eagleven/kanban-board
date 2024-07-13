package com.sparta.kanbanboard.common.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.domain.refreshToken.RefreshTokenRepository;
import com.sparta.kanbanboard.domain.refreshToken.UserRefreshToken;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import com.sparta.kanbanboard.domain.user.utils.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final UserAdapter userAdapter;

    @Value("${JWT-KEY}")
    private String secretKey;

    @Value("${ACCESS-EXPIRATION}")
    private long expirationHours;

    @Value("${REFRESH-EXPIRATION}")
    private long refreshExpirationHours;

    private Key key;
    private long reissueLimit;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
        reissueLimit = refreshExpirationHours * 60 / expirationHours;
    }


    public String createAccessToken(String username, Role role) {
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(username)
                .claim("auth", role.name())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(expirationHours, ChronoUnit.HOURS)))
                .compact();
    }

    public String createRefreshToken(String username, Role role) {
        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(username)
                .claim("auth", role.name())
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(Instant.now().plus(refreshExpirationHours, ChronoUnit.HOURS)))
                .compact();

        UserRefreshToken userRefreshToken = new UserRefreshToken(username, refreshToken);
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken,
                ChronoUnit.HOURS.getDuration().multipliedBy(refreshExpirationHours));
        refreshTokenRepository.save(userRefreshToken);
        return refreshToken;
    }

    public boolean validateToken(String token) {
        log.info("validateToken start");
        log.info("token: {}", token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            // refresh token 활용해서 재발급
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public String reissueAccessToken(String username, Role role) throws IllegalAccessException {
        UserRefreshToken refreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalAccessException("No valid refresh token available."));

        if (refreshToken.getReissueCount() >= reissueLimit) {
            throw new IllegalStateException("Reissue limit for refresh token exceeded");
        }

        return createAccessToken(username, role);
    }

    public String reissueRefreshToken(String username){
        UserRefreshToken refreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));

        if (refreshToken.getReissueCount() >= reissueLimit) {
            throw new RuntimeException("Reissue limit exceeded for refresh token");
        }

        refreshToken.increaseReissueCount();
        refreshTokenRepository.save(refreshToken);

        String newRefreshToken = createRefreshToken(username, userAdapter.findUserByUsername(username).getUserRole());
        refreshToken.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return newRefreshToken;
    }

    public String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]),
                        StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }


    public String extractToken(String headerValue) {
        if (StringUtils.hasText(headerValue) && headerValue.startsWith("Bearer ")) {
            return headerValue.substring(7);
        }
        return headerValue;
    }


    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}

