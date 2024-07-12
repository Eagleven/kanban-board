package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
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
//    private final ColumnAdapter columnAdapter;

    // 모든 카드 조회
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    // 사용자 ID로 카드 조회
    public List<Card> findByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

    // 카드 상태별 조회(columnId 필요)

    // 카드 ID로 카드 조회
    public Card findById(Long cardId) {
        return cardRepository.findById(cardId)
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

    // 카드 저장 (validateCard선언 부분에 Column없어서 일단 주석처리)
    public Card save(Card card) {
        validateCard(card);
        return cardRepository.save(card);
    }


    // 카드 삭제
    public void softDelete(Card card) {
        card.delete();
        cardRepository.save(card);
    }

    // 카드 수정(save 주석처리 중이라 일단 주석처리 함)
    public Card update(Long cardId, Card updatedCard) {
        Card existingCard = findById(cardId);
        if (existingCard.getStatus() == CommonStatusEnum.DELETED) {
            throw new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND);
        }
        updateCardFields(existingCard, updatedCard);
        return save(existingCard);
    }


    // 카드 순서 바꾸기 (Column Id 필요, save 주석처리 되어서 일단 주석해놓음!)
    public void updateCardOrder(Long cardId, /*Long newColumnId,*/ int newPosition) {
        Card card = findById(cardId);
        if (card.getStatus() == CommonStatusEnum.DELETED) {
            throw new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND);
        }

        validateColumnExists(newColumnId);
        card.setColumnId(newColumnId);

        card.setPosition(newPosition);
        save(card);
    }

    // 유효한 카드
    private void validateCard(Card card) {
        if (card.getTitle() == null || card.getContents() == null || card.getColumnId() == null) {
            throw new InvailCardDataException(ResponseExceptionEnum.INVALID_CARD_DATA);
        }
        validateColumnExists(card.getColumnId());
    }


    // 유효한 컬럼인가 -> 컬럼 로직에 있을 것 같아서 *삭제할지 말 지 보류* -> 이거 Card 에도 필요할까요?
    private void validateColumnExists(Long columnId) {
        if (!columnAdapter.existsById(columnId)) {
            throw new ColumnNotFoundException(ResponseExceptionEnum.COLUMN_NOT_FOUND);
        }
    }


    private void updateCardFields(Card existingCard, Card updatedCard) {
        if (updatedCard.getTitle() != null) {
            existingCard.setTitle(updatedCard.getTitle());
        }
        if (updatedCard.getContents() != null) {
            existingCard.setContents(updatedCard.getContents());
        }
        if (updatedCard.getDueDate() != null) {
            existingCard.setDueDate(updatedCard.getDueDate());
        }
        if (updatedCard.getUser() != null) {
            existingCard.setUser(updatedCard.getUser());
        }

        if (updatedCard.getColumnId() != null) {
            validateColumnExists(updatedCard.getColumnId());
            existingCard.setColumnId(updatedCard.getColumnId());
        }


    }

}


