package com.sparta.kanbanboard.domain.refreshToken;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

// userId를 통해서 빠르게 서치한다!는 전략
public interface RefreshTokenRepository extends CrudRepository<UserRefreshToken, String> {
    Optional<UserRefreshToken> findByUsername(String username);
}