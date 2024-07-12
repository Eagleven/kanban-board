package com.sparta.kanbanboard.common.security.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.config.redis.RedisUtil;
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
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private final UserAdapter userAdapter;
    @Value("${JWT-KEY}")
    private String secretKey;

    @Value("${ACCESS-EXPIRATION}")
    private long expirationHours;

    @Value("${REFRESH-EXPIRATION}")
    private long refreshExpirationHours;

    private final RedisTemplate<String, String> redisTemplate;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final RedisUtil redisUtil;

    private Key key;
    private long reissueLimit;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
        reissueLimit = refreshExpirationHours * 60 / expirationHours;
    }

    public String createAccessToken(String username, Role role) {
        return "Bearer " + Jwts.builder()
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

        tokenService.saveRefreshToken(username, refreshToken,
                ChronoUnit.HOURS.getDuration().multipliedBy(refreshExpirationHours).toMillis());
        UserRefreshToken userRefreshToken = new UserRefreshToken(username, refreshToken);
        refreshTokenRepository.save(userRefreshToken);
        return "Bearer " + refreshToken;
    }

    public void invalidateTokens(String username, String accessToken) {
        tokenService.invalidateAccessToken(accessToken);
        tokenService.invalidateRefreshToken(username);
    }

    public boolean validateToken(String token) {
        log.info("validateToken start");
        log.info("token: {}", token);
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
                    .getBody();
            // 블랙리스트 확인
            if (tokenService.isAccessTokenInvalidated(token)) {
                return false;
            }
            return !claims.getExpiration().before(new Date());
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

    public String reissueRefreshToken(String username) {
        UserRefreshToken refreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));

        if (refreshToken.getReissueCount() >= reissueLimit) {
            throw new RuntimeException("Reissue limit exceeded for refresh token");
        }

        refreshToken.increaseReissueCount();
        refreshTokenRepository.save(refreshToken);

        String newRefreshToken = createRefreshToken(username,
                userAdapter.findUserByUsername(username).getUserRole());
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

    public String getAccessTokenFromHeader(HttpServletRequest request)
            throws IllegalAccessException {
        String accessToken = request.getHeader("AccessToken");
        if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
            return accessToken.substring(7); // 헤더인 Bearer 을 잘라서 가져온다.
        } else {
            Role role = Role.valueOf(getUserInfoFromToken(accessToken).get("auth").toString());
            reissueAccessToken(getUserInfoFromToken(accessToken).getSubject(), role);
        }
        return accessToken;
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        if (StringUtils.hasText(refreshToken) && refreshToken.startsWith("Bearer ")) {
            return refreshToken.substring(7);
        } else {
            reissueRefreshToken(getUserInfoFromToken(refreshToken).getSubject());
        }
        return refreshToken;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public void setHeaderAccessToken(HttpServletResponse response, String newAccessToken) {
        response.setHeader("AccessToken", newAccessToken);
    }

    public Long getExpiration(String accessToken){

        Date expiration = Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }
}
