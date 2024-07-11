package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardAdapter cardAdapter;
//    private final ColumnAdapter columnAdapter;

    // 컬럼이랑 연관관계 맺어서 수정해야할 것들 + user받아서 맨 밑에 시큐리티 로그인한 사용자랑 같은지 받는것만 고치면 됨(예외처리때문에)

    // 모든 카드 조회
    @Transactional(readOnly = true)
    public List<Card> getAllCards() {
        return cardAdapter.findAll();
    }

    // 사용자별 카드 조회
    @Transactional(readOnly = true)
    public List<Card> getCardByUser(Long userId) {
        return cardAdapter.findByUserId(userId);
    }

    // 카드 상태별 조회 -> Column 데려와서 수정
   /*
   @Transactional(readOnly = true)
    public List<Card> getCardsByStatus(CardStatus status) {
        return cardAdapter.findByStatus(status);}
    */


    // 카드 생성
    @Transactional
    public Card createCard(Card card) {
        return cardAdapter.save(card);
    }

    // 카드 수정
    @Transactional
    public Card updateCard(Long cardId, Card card) {
        Card existingCard = cardAdapter.findById(cardId);
        existingCard.setTitle(card.getTitle());
        existingCard.setContents(card.getContents());
        // 상태 업데이트가 필요한 경우 주석 해제
        // existingCard.setStatus(card.getStatus());
        return cardAdapter.save(existingCard);
    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId) {
        cardAdapter.delete(cardId);
    }


}
