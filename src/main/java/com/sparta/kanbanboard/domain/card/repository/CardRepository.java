package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByUserId(Long userId);

    List<Card> findByStatus(CommonStatusEnum status);

    List<Card> findByStatusAndUserId(CommonStatusEnum status, Long userId);

    List<Card> findByColumn(Column column);

}
