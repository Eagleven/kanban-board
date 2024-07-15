package com.sparta.kanbanboard.domain.refreshToken;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 14440) // 4hours
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRefreshToken {

    @Id
    private String username;

    private String refreshToken;

    private Long reissueCount = 0L;


    public UserRefreshToken(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.reissueCount++;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    /**
     * reissue의 횟수를 측정하여 제한하기 위함입니다.
     */
    public void increaseReissueCount() {
        ++reissueCount;
    }
}