package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.dto.CardResponseDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.card.CardUpdateFailureException;
import com.sparta.kanbanboard.exception.user.UnauthorizedAccessException;
import com.sparta.kanbanboard.exception.userandboard.UserNotBoardMemberException;
import java.util.List;
import java.util.stream.Collectors;
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
    private final CardRepository cardRepository;
    private final UserAndBoardAdapter userAndBoardAdapter;

    // 모든 카드 조회
    @Transactional(readOnly = true)
    public List<CardResponseDto> getAllCards() {
        return cardAdapter.findByStatus(CommonStatusEnum.ACTIVE).stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상태별 카드 조회
    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardsByColumn(Long columnId) {
        Column column = columnAdapter.findById(columnId);
        return cardRepository.findByColumnAndStatus(column, CommonStatusEnum.ACTIVE).stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 사용자별 카드 조회
    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardByUser(Long userId) {
        return cardAdapter.findActiveCardsByUserId(userId).stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 카드 생성
    @Transactional
    public CardResponseDto createCard(Long columnId, CardRequestDto cardRequestDto, User user) {
        checkUserAuthentication();
        Column column = columnAdapter.findById(columnId);
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), column.getBoard().getId())){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        Card card = Card.builder()
                .title(cardRequestDto.getTitle())
                .contents(cardRequestDto.getContents())
                .column(column)
                .sequence(0) // 초기 순서 설정
                .user(user) // 사용자 설정
                .build();
        Card savedCard = cardAdapter.save(card);
        return new CardResponseDto(savedCard);
    }

    // 카드 수정
    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto cardRequestDto, User user) {
        checkUserAuthentication();
        Card card = cardAdapter.findById(cardId);
        if (!user.getUsername().equals(card.getUser().getUsername()) && !user.getUserRole().equals(Role.MANAGER)) {
            throw new CardUpdateFailureException(ResponseExceptionEnum.CARD_UPDATE_FAILURE);
        }
        return new CardResponseDto(cardAdapter.update(card, cardRequestDto));
    }

//    // 카드 위치 수정
//    @Transactional
//    public void updateCardOrder(Long cardId, Long newColumnId, int newSequence) {
//        checkUserAuthentication();
//        Column newColumn = columnAdapter.findById(newColumnId);
//        cardAdapter.updateCardOrder(cardId, newColumnId, newSequence);
//    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId, User user) {
        checkUserAuthentication();
        Card card = cardAdapter.findById(cardId);
        if (!user.getUsername().equals(card.getUser().getUsername()) && !user.getUserRole().equals(Role.MANAGER)) {
            throw new CardUpdateFailureException(ResponseExceptionEnum.CARD_DELETE_FAILURE);
        }
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