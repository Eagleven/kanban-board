package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.service.CardService;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;
// ResponseCodeEnum추가하고(o), 로직 추가하고(o), url 확인해서 추가하고, builder빨간줄 해결!

    // 카드 생성
    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CardRequestDto cardRequestDto) {
        Card card = cardService.createCard(toEntity(cardRequestDto));
        return ResponseUtils.of(ResponseCodeEnum.CARD_CREATE_SUCCESS, card);
    }

    // 모든 카드 조회
    @GetMapping
    public ResponseEntity<?> getAllcards() {
        List<Card> cards = cardService.getAllCards();
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_ALL_SUCCESS, cards);
    }

    // 작업자별 카드 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getCardsByUser(@PathVariable Long userId) {
        List<Card> cards = cardService.getCardByUser(userId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_USER_SUCCESS, cards);
    }

    // 상태별 카드 조회 (Column과 합친 후에 완성 가능)
    /*
    @GetMapping("/{columnId}")
    public ResponseEntity<?> getCardsByStatus(@PathVariable ColumnStatus status) {
        List<Card> cards = cardService.getCardByStatus(status);
        return ResponseUtils.of(ResponseCodeEnum.CARD_GET_STATUS_SUCCESS, cards);
    }
     */

    // 카드 수정
    @PutMapping("/{cardId}")
    public ResponseEntity<?> updateCard(@PathVariable Long cardId,
            @RequestBody CardRequestDto cardRequestDto) {
        Card updatedCard = cardService.UpdateCard(cardId, toEntity(cardRequestDto));
        return ResponseUtils.of(ResponseCodeEnum.CARD_UPDATE_SUCCESS, updatedCard);
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseUtils.of(ResponseCodeEnum.CARD_DELETE_SUCCESS);
    }

    @Builder
    private Card toEntity(CardRequestDto dto) {
        return Card.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
//                .columnId(dto.getColumnId())
                .build();
    }
}
