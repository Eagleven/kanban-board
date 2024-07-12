package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.exception.card.CardNotFoundException;
import com.sparta.kanbanboard.exception.card.InvailCardDataException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardAdapter {

    private final CardRepository cardRepository;

    // 모든 카드 조회
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    // 사용자 ID로 카드 조회.

    public List<Card> findByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

//    public List<Card> findByStatus(CardStatus status) {
//    return cardRepository.findByStatus(status);
//}

    // ID로 카드 조회 (없으면 예외 발생)
    public Card findById(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND));
    }

    // 카드 저장 (유효성 검사 포함)
    public Card save(Card card) {
        if (card.getTitle() == null && card.getContents() == null/* && card.getStatus() == null*/) {
            throw new InvailCardDataException(ResponseExceptionEnum.INVALID_CARD_DATA);
            // 컬럼 ID를 통해 컬럼이 존재하는지 확인
            // Column column = columnRepository.findById(card.getColumnId())
            //     .orElseThrow(() -> new ColumnNotFoundException(리스폰스익셉션으로받아서.컬럼낫파운드(이건 컬럼에서 지정해야할 것 같아서 따로 안만듦)));

        }
        return cardRepository.save(card);
    }

    // 카드 삭제
    public void delete(Long cardId) {
        Card card = findById(cardId);
        cardRepository.delete(card);
    }


}
