package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ColumnRepository extends JpaRepository<Column, Long> {
    @Query("SELECT c FROM Column c JOIN FETCH c.board WHERE c.board = :board AND c.status = :status")
    List<Column> findByBoardAndStatus(@Param("board") Board board, @Param("status") CommonStatusEnum status);

    Column findByBoardAndStatusAndNextIsNull(Board board, CommonStatusEnum status);
}