package com.sparta.kanbanboard.domain.board.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.exception.board.BoardNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardAdapter {

    private final BoardRepository boardRepository;

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .filter(board -> board.getStatus() == CommonStatusEnum.ACTIVE)
                .orElseThrow(() -> new BoardNotFoundException(ResponseExceptionEnum.BOARD_NOT_FOUND));
    }

    public Page<Board> findByIdIn(List<Long> boardIdList, Pageable pageable, CommonStatusEnum status){
        return boardRepository.findByIdInAndStatus(boardIdList, pageable, status);
    }

    public void delete(Board board) {
        board.delete(board);
        boardRepository.save(board);
    }
}
