package com.sparta.kanbanboard.domain.user.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByIdAndStatus(Long id, CommonStatusEnum status);

    Page<User> findAllByStatus(CommonStatusEnum status, Pageable pageable);
}
