package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.exception.board.BoardNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardAdapter {

    private final BoardRepository boardRepository;

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public Board findById(Long id){
        return boardRepository.findById(id)
                .orElseThrow(()-> new BoardNotFoundException(ResponseExceptionEnum.BOARD_NOT_FOUND));
    }
}
