package com.sparta.kanbanboard.domain.userandboard.repository;

import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAndBoardAdapter {

    private final UserAndBoardRepository userAndBoardRepository;

    public UserAndBoard save(UserAndBoard userAndBoard) {
        return userAndBoardRepository.save(userAndBoard);
    }

    public List<UserAndBoard> findByUserId(Long userId) {
        return userAndBoardRepository.findByUserId(userId);
    }

    public boolean existsByUserIdAndBoardId(Long userId, Long boardId){
        return userAndBoardRepository.existsByUserIdAndBoardId(userId, boardId);
    }
}
