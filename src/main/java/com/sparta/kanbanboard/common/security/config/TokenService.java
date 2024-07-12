package com.sparta.kanbanboard.common.security.config;

import com.sparta.kanbanboard.config.redis.RedisUtil;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(RedisTemplate<String, String> redisTemplate, RedisUtil redisUtil) {
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void invalidateAccessToken(String accessToken) {
        // 블랙리스트에 Access Token 추가
        redisTemplate.opsForValue().set(accessToken, "true", 1, TimeUnit.HOURS); // 1시간 동안 유효
    }

    public boolean isAccessTokenInvalidated(String accessToken) {
        return redisTemplate.hasKey(accessToken);
    }

    @Transactional
    public void saveRefreshToken(String username, String refreshToken, long expirationTime) {
        redisTemplate.opsForValue().set("refreshToken:" + username, refreshToken, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public void invalidateRefreshToken(String username) {
        redisTemplate.delete("refreshToken:" + username);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("refreshToken:" + username);
    }
}
