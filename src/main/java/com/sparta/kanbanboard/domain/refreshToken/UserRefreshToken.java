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
    // 중간 테이블 : 조회하거나, 쿼리를 날릴때 패치하는 전략에 따라서 N+1과 같은 문제를 방지하기 위해서 사용합니다.
    // 다대다 : 중간테이블을 뒀을 때 장점은 엔티티로 관리를 하기 때문에 객체안의 객체를 관리하여 무수한 데이터를 관리할 수 있다.
    // redis, nosql indexing 방법 조회
    // 전제 목록 조회의 상황에서 여러가지 기준을 선정할 수 있을 때 이렇게 인덱싱하는 방법에 따라서 속도의 차이가 날 수 있다.
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