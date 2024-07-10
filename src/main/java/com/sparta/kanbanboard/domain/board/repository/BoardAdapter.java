package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardAdapter {

    private final BoardRepository boardRepository;

    public Board save(Board board) {
        return boardRepository.save(board);
    }
}
