package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.domain.card.entity.Card;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardAdapter {
private final CardRepository cardRepository;

// 예외처리도 이곳에서 해줘야함!
public List<Card> findAll() {
    return cardRepository.findAll();
}

public List<Card> findByUserId(Long userId) {
    return cardRepository.findByUserId(userId);
}

//    public List<Card> findByStatus(CardStatusEnum status) {
//    return cardRepository.findByStatus(status);
//}

public Optional<Card> findById(Long cardId) {
    return cardRepository.findById(cardId);
}

public Card save(Card card) {
    return cardRepository.save(card);
}

public void delete(Card card) {
    cardRepository.delete(card);
}






}
