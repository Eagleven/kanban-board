package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.dto.CardResponseDto;
import com.sparta.kanbanboard.domain.card.service.CardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<HttpResponseDto> createCard(@RequestBody CardRequestDto cardRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getUser().getId(); // UserDetailsImpl에서 User 객체의 ID 가져오기

        CardResponseDto cardResponseDto = cardService.createCard(cardRequestDto, userId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_CREATE_SUCCESS, cardResponseDto);
    }

    // 모든 카드 조회
    @GetMapping
    public ResponseEntity<HttpResponseDto> getAllcards() {
        List<CardResponseDto> cards = cardService.getAllCards();
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_ALL_SUCCESS, cards);
    }

    // 작업자별 카드 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<HttpResponseDto> getCardsByUser(@PathVariable Long userId) {
        List<CardResponseDto> cards = cardService.getCardByUser(userId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_USER_SUCCESS, cards);
    }

    // 컬럼별 상태 카드 조회
    @GetMapping("/column/{columnId}")
    public ResponseEntity<HttpResponseDto> getCardsByColumn(@PathVariable Long columnId) {
        List<CardResponseDto> cards = cardService.getCardsByColumn(columnId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_COLUMN_GET_STATUS_SUCCESS, cards);
    }

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> updateCard(@PathVariable Long cardId,
            @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto updatedCard = cardService.updateCard(cardId, cardRequestDto);
        return ResponseUtils.of(ResponseCodeEnum.CARD_UPDATE_SUCCESS, updatedCard);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_DELETE_SUCCESS);
    }
}
