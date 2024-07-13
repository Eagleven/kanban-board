package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    /*
     * status 필드가 특정 값인 모든 레코드를 찾아서,
     * 그 레코드들을 sequence 필드를 기준으로 오름차순으로 정렬
     */
    List<Column> findAllByStatus(CommonStatusEnum status);

    List<Column> findByBoardAndStatus(Board board, CommonStatusEnum status);
    Column findByBoardAndStatusAndNextIsNull(Board board, CommonStatusEnum status);
}