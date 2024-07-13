package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.exception.user.UnauthorizedAccessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardAdapter cardAdapter;
    private final ColumnAdapter columnAdapter;


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

    // 컬럼별 카드 상태 조회 (시작 전 진행중 완료)
    @Transactional(readOnly = true)
    public List<Card> getCardsByColumn(Long columnId) {
        return cardAdapter.findByColumn(columnId);
    }

    // 카드 삭제 상태별 조회 (ACTIVE || DELETE)
    @Transactional(readOnly = true)
    public List<Card> getCardsByStatus(CommonStatusEnum status) {
        return cardAdapter.findByStatus(status);
    }

    // 사용자별 ACTIVE 상태의 카드 조회
    @Transactional(readOnly = true)
    public List<Card> getActiveCardsByUser(Long userId) {
        return cardAdapter.findActiveCardsByUserId(userId);
    }

    // 모든 ACTIVE 카드 조회
    @Transactional(readOnly = true)
    public List<Card> getAllActiveCards() {
        return cardAdapter.findActiveCards();
    }

    // 카드 생성
    @Transactional
    public Card createCard(CardRequestDto cardRequestDto) {
        checkUserAuthentication();
        Column column = columnAdapter.findById(cardRequestDto.getColumnId());
        Card card = Card.builder()
                .title(cardRequestDto.getTitle())
                .contents(cardRequestDto.getContents())
                .column(column)
                .sequence(0) // 초기 순서 설정
                .build();
        return cardAdapter.save(card);
    }

    // 카드 수정
    @Transactional
    public Card updateCard(Long cardId, CardRequestDto cardRequestDto) {
        checkUserAuthentication();
        Column column = columnAdapter.findById(cardRequestDto.getColumnId());
        return cardAdapter.update(cardId, cardRequestDto, column);
    }

    // 카드 위치 수정
    @Transactional
    public void updateCardOrder(Long cardId, Long newColumnId, int newSequence) {
        checkUserAuthentication();
        cardAdapter.updateCardOrder(cardId, newColumnId, newSequence);
    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId) {
        checkUserAuthentication();
        Card card = cardAdapter.findById(cardId);
        cardAdapter.softDelete(card);
    }

    private void checkUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException(
                    ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO);
        }
    }


}


