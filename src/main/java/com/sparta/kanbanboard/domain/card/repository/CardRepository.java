package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

}
