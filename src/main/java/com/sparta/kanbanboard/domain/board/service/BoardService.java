package com.sparta.kanbanboard.domain.board.service;

import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.dto.BoardResponseDto;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.board.repository.BoardAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardAdapter boardAdapter;


    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = new Board(requestDto.getName(), requestDto.getExplanation());
        System.out.println(board.getExplanation());
        return new BoardResponseDto(boardAdapter.save(board));
    }
}
