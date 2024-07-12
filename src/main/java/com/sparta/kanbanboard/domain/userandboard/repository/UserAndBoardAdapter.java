package com.sparta.kanbanboard.domain.userandboard.repository;

import com.sparta.kanbanboard.domain.userandboard.entity.UserAndBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAndBoardAdapter {

    private final UserAndBoardRepository userAndBoardRepository;

    public UserAndBoard save(UserAndBoard userAndBoard) {
        return userAndBoardRepository.save(userAndBoard);
    }
    public List<UserAndBoard> findByUserId(Long userId){
        return userAndBoardRepository.findByUserId(userId);
    }
    public UserAndBoard findByUserIdAndBoardId(Long userId, Long boardId){
        return userAndBoardRepository.findByUserIdAndBoardId(userId, boardId);
    }
}
