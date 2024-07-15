package com.sparta.kanbanboard.domain.board.dto;

import com.sparta.kanbanboard.domain.board.entity.Board;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BoardResponseDto {
    private Long id;
    private String name;
    private String explanation;

    public BoardResponseDto(Board board) {
        this.id=board.getId();
        this.name = board.getName();
        this.explanation = board.getExplanation();
    }
}
