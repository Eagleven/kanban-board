package com.sparta.kanbanboard.common.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.domain.refreshToken.RefreshTokenRepository;
import com.sparta.kanbanboard.domain.refreshToken.UserRefreshToken;
import com.sparta.kanbanboard.domain.user.utils.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${JWT-KEY}")
    private String secretKey;

    @Value("${ACCESS-EXPIRATION}")
    private long expirationHours;

    @Value("${REFRESH-EXPIRATION}")
    private long refreshExpirationHours;

    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final com.sparta.kanbanboard.common.security.config.TokenService tokenService;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username, Role role) {
        return "Bearer " + Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(username)
                .claim("auth", role.name())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusMillis(expirationHours)))
                .compact();
    }

    public String createRefreshToken(String username, Role role) {
        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512)
                .setSubject(username)
                .claim("auth", role.name())
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(Instant.now().plusMillis(refreshExpirationHours)))
                .compact();

        tokenService.saveRefreshToken(username, refreshToken,refreshExpirationHours);
        UserRefreshToken userRefreshToken = new UserRefreshToken(username, refreshToken);
        refreshTokenRepository.save(userRefreshToken);
        return "Bearer " + refreshToken;
    }

//    public void invalidateTokens(String username) {
//        tokenService.(username);
//    }

    public boolean validateToken(String token) {
        log.info("validateToken start");
        log.info("token: {}", token);
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                    .getBody();
            if (tokenService.hasRefreshToken(token)) {
                return false;
            }
            return !claims.getExpiration().before(new Date());
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public String decodeJwtPayloadSubject(String oldAccessToken) throws JsonProcessingException {
        return objectMapper.readValue(
                new String(Base64.getDecoder().decode(oldAccessToken.split("\\.")[1]),
                        StandardCharsets.UTF_8),
                Map.class
        ).get("sub").toString();
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessToken = request.getHeader("AccessToken");
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            return accessToken.substring(7); // 헤더인 Bearer 을 잘라서 가져온다.
        }

        return accessToken;
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            return refreshToken.substring(7);
        }

        return refreshToken;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public void setHeaderAccessToken(HttpServletResponse response, String newAccessToken) {
        response.setHeader("AccessToken", newAccessToken);
    }

    public Long getExpiration(String accessToken) {

        Date expiration = Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    public void invalidateTokens(String username, String accessToken) {
        tokenService.invalidateAccessToken(accessToken);
        tokenService.invalidateRefreshToken(username);
    }
}
