package com.sparta.kanbanboard.domain.user.repository;

import com.sparta.kanbanboard.domain.user.DeletedUser;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeletedUserRepository extends JpaRepository<DeletedUser, Long>{

    Optional<DeletedUser> findUserByUsername(String username);

    @Query("SELECT du FROM DeletedUser du WHERE du.createdAt < :expirationTime")
    List<DeletedUser> findExpiredUsers(LocalDateTime expirationTime);
}
