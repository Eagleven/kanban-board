package com.sparta.kanbanboard.domain.board.service;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.dto.BoardResponseDto;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import com.sparta.kanbanboard.exception.board.BoardAlreadyDeletedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardAdapter boardAdapter;


    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto.getName(), requestDto.getExplanation());
        System.out.println(board.getExplanation());
        return new BoardResponseDto(boardAdapter.save(board));
    }

    public Page<BoardResponseDto> getBoardList() {
        return null; // 사용자 정보가 필요해서 추구에 개발
    }


    @Transactional
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto requestDto) {
        // 요청한 사용자가 해당 보드의 생성자인지 확인하는 로직 필요


        // 삭제 처리된 보드인지 확인
        Board board = boardAdapter.findById(boardId);
        if(board.getStatus().equals(CommonStatusEnum.DELETED)){
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }
        board.update(requestDto);
        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        // 요청한 사용자가 해당 보드의 생성자인지 확인하는 로직 필요

        Board board= boardAdapter.findById(boardId);

        // 삭제 처리된 보드인지 확인
        if(board.getStatus().equals(CommonStatusEnum.DELETED)){
            throw new BoardAlreadyDeletedException(ResponseExceptionEnum.BOARD_ALREADY_DELETED);
        }
        board.delete();
    }
}
