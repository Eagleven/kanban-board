package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    List<Column> findByBoardAndStatus(Board board, CommonStatusEnum status);
    Column findByBoardAndStatusAndNextIsNull(Board board, CommonStatusEnum status);
}