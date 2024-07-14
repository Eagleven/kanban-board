package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.exception.card.CardNotFoundException;
import com.sparta.kanbanboard.exception.card.ColumnNotFoundException;
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

    // 카드 ID로 카드 조회
    public Card findById(Long cardId) {
        return cardRepository.findById(cardId)
                .filter(card -> card.getStatus().equals(CommonStatusEnum.ACTIVE))
                .orElseThrow(() -> new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND));
    }

    // 카드 삭제 여부 조회 (ACTIVE || DELETE)
    public List<Card> findByStatus(CommonStatusEnum status) {
        return cardRepository.findByStatus(status);
    }

    // 모든 ACTIVE 카드 조회
    public List<Card> findActiveCards() {
        return cardRepository.findByStatus(CommonStatusEnum.ACTIVE);
    }

    // 사용자 ID 별로 ACTIVE 카드 조회
    public List<Card> findActiveCardsByUserId(Long userId) {
        return cardRepository.findByStatusAndUserId(CommonStatusEnum.ACTIVE, userId);
    }

    // 카드 저장
    public Card save(Card card) {
        validateCard(card);
        return cardRepository.save(card);
    }

    // 카드 삭제
    public void softDelete(Card card) {
        card.delete();
        cardRepository.save(card);
    }

    // 카드 수정
    public Card update(Card card, CardRequestDto cardRequestDto) {
        updateCardFields(card, cardRequestDto);
        return save(card);
    }


//    // 카드 순서 변경
//    public void updateCardOrder(Long cardId, Long newColumnId, int newSequence) {
//        Card card = findById(cardId);
//        if (card.getStatus() == CommonStatusEnum.DELETED) {
//            throw new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND);
//        }
//        validateColumnExists(newColumnId);
//        card.setColumn(newColumnId);
//        card.setSequence(newSequence);
//        save(card);
//    }

    // 유효한 카드
    private void validateCard(Card card) {
        if (card.getTitle() == null) {
            throw new InvailCardDataException(ResponseExceptionEnum.INVALID_CARD_DATA);
        }
    }

    private void updateCardFields(Card existingCard, CardRequestDto cardRequestDto) {
        if (cardRequestDto.getTitle() != null) {
            existingCard.setTitle(cardRequestDto.getTitle());
        }
        if (cardRequestDto.getContents() != null) {
            existingCard.setContents(cardRequestDto.getContents());
        }
        if (cardRequestDto.getDueDate() != null) {
            existingCard.setDueDate(cardRequestDto.getDueDate());
        }
    }
}