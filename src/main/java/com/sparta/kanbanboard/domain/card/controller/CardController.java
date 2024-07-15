package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.dto.CardResponseDto;
import com.sparta.kanbanboard.domain.card.service.CardService;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final UserAdapter userAdapter;


    // 카드 생성
    @PostMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> createCard(
            @PathVariable("columnId") Long columnId,
            @RequestBody CardRequestDto cardRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        CardResponseDto cardResponseDto = cardService.createCard(columnId, cardRequestDto,
                userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.CARD_CREATE_SUCCESS, cardResponseDto);
    }

    @GetMapping("/download/{cardId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long cardId) {
        return cardService.downloadFile(cardId);
    }

    // 모든 카드 조회
    @GetMapping
    public ResponseEntity<HttpResponseDto> getAllcards() {
        List<CardResponseDto> cards = cardService.getAllCards();
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_ALL_SUCCESS, cards);
    }

    // 컬럼별 카드 조회
    @GetMapping("/column/{columnId}")
    public ResponseEntity<HttpResponseDto> getCardsByColumn(
            @PathVariable("columnId") Long columnId) {
        List<CardResponseDto> cards = cardService.getCardsByColumn(columnId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_ALL_SUCCESS, cards);
    }

    // 작업자별 카드 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<HttpResponseDto> getCardsByUser(@PathVariable Long userId) {
        userAdapter.findById(userId);
        List<CardResponseDto> cards = cardService.getCardByUser(userId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_USER_SUCCESS, cards);
    }

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> updateCard(
            @PathVariable("cardId") Long cardId,
            @ModelAttribute CardRequestDto cardRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        CardResponseDto updatedCard = cardService.updateCard(cardId, cardRequestDto,
                userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.CARD_UPDATE_SUCCESS, updatedCard);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<HttpResponseDto> deleteCard(@PathVariable Long cardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        cardService.deleteCard(cardId, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.CARD_DELETE_SUCCESS);
    }

}