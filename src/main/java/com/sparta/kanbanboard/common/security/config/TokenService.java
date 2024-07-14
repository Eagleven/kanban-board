package com.sparta.kanbanboard.common.security.config;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getRefreshToken(String username) {
        try {
            return (String) redisTemplate.opsForHash().get("refreshToken:" + username, "refreshToken");
        } catch (Exception e) {
            log.error("Error retrieving key from Redis: {}", "refreshToken:" + username, e);
            return null;
        }

    }

    public boolean deleteRefreshToken(String username) {
        return Boolean.TRUE.equals(redisTemplate.delete("refreshToken:" + username));
    }

    public boolean hasRefreshToken(String username) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("refreshToken:" + username));
    }

    public boolean isAccessTokenInvalidated(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(accessToken));
    }

    @Transactional
    public void saveRefreshToken(String username, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public void invalidateRefreshToken(String username) {
        redisTemplate.delete("refreshToken:" + username);
    }

    @Transactional
    public void invalidateAccessToken(String accessToken) {
        redisTemplate.opsForValue().set(accessToken, "true", 1, TimeUnit.HOURS); // 블랙리스트에 1시간 동안 유지
    }
}