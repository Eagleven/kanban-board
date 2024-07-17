package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByStatus(CommonStatusEnum status);

    @Query("SELECT c FROM Card c WHERE c.status = :status AND c.user.id = :userId")
    List<Card> findByStatusAndUserId(@Param("status") CommonStatusEnum status, @Param("userId") Long userId);

    List<Card> findByColumnAndStatus(Column column, CommonStatusEnum status);

}