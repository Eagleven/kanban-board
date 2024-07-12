package com.sparta.kanbanboard.domain.userandboard.repository;

import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAndBoardRepository extends JpaRepository<UserAndBoard, Long> {
    List<UserAndBoard> findByUserId(Long userId);
    Optional<UserAndBoard> findByUserIdAndBoardId(Long userId, Long BoardId);
}
