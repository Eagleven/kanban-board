package com.sparta.kanbanboard.domain.boardandcolumn.entity;

import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.column.entity.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BoardAndColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "columns_id", nullable = false)
    private Column column;

    public BoardAndColumn(Board board, Column column) {
        this.board = board;
        this.column = column;
    }
}
