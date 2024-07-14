package com.sparta.kanbanboard.domain.card.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
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
    private final ColumnAdapter columnAdapter;

    // 모든 카드 조회
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    // 사용자 ID로 카드 조회
    public List<Card> findByUserId(Long userId) {
        return cardRepository.findByUserId(userId);
    }

    // 컬럼별 카드 상태 조회 (시작 전 진행중 완료)
    public List<Card> findByColumn(Long columnId) {
        validateColumnExists(columnId);
        Column column = columnAdapter.findById(columnId);
        return cardRepository.findByColumn(column);
    }

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
    public Card update(Long cardId, CardRequestDto updatedCard, Column column) {
        Card existingCard = findById(cardId);
        if (existingCard.getStatus() == CommonStatusEnum.DELETED) {
            throw new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND);
        }
        updateCardFields(existingCard, updatedCard, column);
        return save(existingCard);
    }


    // 카드 순서 변경
    public void updateCardOrder(Long cardId, Long newColumnId, int newSequence) {
        Card card = findById(cardId);
        if (card.getStatus() == CommonStatusEnum.DELETED) {
            throw new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND);
        }
        validateColumnExists(newColumnId);
        card.setColumn(newColumnId);
        card.setSequence(newSequence);
        save(card);
    }

    // 유효한 카드
    private void validateCard(Card card) {
        if (card.getTitle() == null || card.getContents() == null || card.getColumn() == null) {
            throw new InvailCardDataException(ResponseExceptionEnum.INVALID_CARD_DATA);
        }
        validateColumnExists(card.getColumn().getId());
    }


    // 유효한 컬럼인가 -> 컬럼 로직에 있을 것 같아서 *삭제할지 말 지 보류* -> 이거 Card 에도 필요할까요?
    private void validateColumnExists(Long columnId) {
        if (!columnAdapter.existById(columnId)) {
            throw new ColumnNotFoundException(ResponseExceptionEnum.COLUMN_NOT_FOUND);
        }
    }


    private void updateCardFields(Card existingCard, CardRequestDto updatedCard, Column column) {
        if (updatedCard.getTitle() != null) {
            existingCard.setTitle(updatedCard.getTitle());
        }
        if (updatedCard.getContents() != null) {
            existingCard.setContents(updatedCard.getContents());
        }
        if (updatedCard.getDueDate() != null) {
            existingCard.setDueDate(updatedCard.getDueDate());
        }
//        if (updatedCard.getUserId() != null) {
//            existingCard.setUser(updatedCard.getUserId());
//        }

        if (updatedCard.getColumnId() != null) {
            validateColumnExists(updatedCard.getColumnId());
            existingCard.setColumn(updatedCard.getColumnId());
        }


    }

}