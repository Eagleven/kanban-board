package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.column.entity.Column;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {

    /*
     * status 필드가 특정 값인 모든 레코드를 찾아서,
     * 그 레코드들을 sequence 필드를 기준으로 오름차순으로 정렬
     */
    List<Column> findAllByStatusOrderBySequenceAsc(CommonStatusEnum status);

    /*
     * sequence 필드의 값이 특정 값보다 크고,
     * status 필드의 값이 특정 값과 일치하는 모든 레코드를 찾아서,
     * 그 레코드들을 sequence 필드를 기준으로 오름차순으로 정렬
     */
    List<Column> findBySequenceGreaterThanAndStatusOrderBySequenceAsc(int sequence, CommonStatusEnum status);
}
