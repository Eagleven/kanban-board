package com.sparta.kanbanboard.domain.userandboard.repository;

import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAndBoardRepository extends JpaRepository<UserAndBoard, Long> {
    List<UserAndBoard> findByUserId(Long userId);
    boolean existsByUserIdAndBoardId(Long userId, Long boardId);
}
