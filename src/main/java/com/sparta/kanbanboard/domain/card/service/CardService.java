package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.exception.card.CardNotFoundException;
import com.sparta.kanbanboard.exception.card.InvailCardDataException;
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
    // 카드 전체 조회
    @Transactional
    public List<Card> getAllCards() {
        return cardAdapter.findAll();
    }

    // 카드 작업자별 조회
    @Transactional
    public List<Card> getCardByUser(Long userId) {
        return cardAdapter.findByUserId(userId);
    }

    // 카드 상태별 조회 -> Column 데려와서 수정
   /* @Transactional
    public List<Card> getCardsByStatus(CardStatus status) {
        return cardAdapter.findByStatus(status);
    }
    */


    // 카드 생성
    public Card createCard(Card card)
            throws InvailCardDataException /*, ColumnsNotFoundException*/ {
        if (card.getTitle() == null
                && card.getContents() == null /* && card.getStatus() == null*/) {
            throw new InvailCardDataException(ResponseExceptionEnum.INVALID_CARD_DATA);
        }
        // 컬럼 ID를 통해 컬럼이 존재하는지 확인
        // Column column = columnRepository.findById(card.getColumnId())
        //     .orElseThrow(() -> new ColumnNotFoundException("Column not found"));

        return cardAdapter.save(card);
    }

    // 카드 수정
    public Card UpdateCard(Long cardId, Card card)
            throws CardNotFoundException/*, UnauthorizedAccessException/*, ColumnNotFoundException*/ {
        Card existingCard = cardAdapter.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND));
    /*
        if (!isLoggedInUserAuthorized(existingCard.getUserId())) {
           throw new UnauthorizedAccessException(ResponseExceptionEnum.UNAUTHORIZED_ACCESS);
       }

     */

   /*
       // Column validation
        Column column = columnRepository.findById(card.getColumnId())
                .orElseThrow(() -> new ColumnNotFoundException(ResponseExceptionEnum.COLUMN_NOT_FOUND));
      */

        existingCard.setTitle(card.getTitle());
        existingCard.setContents(card.getContents());
//        **질문** 컬럼 상태 업데이트 필요시 추가하려는 로직인데 existingColumn.setStatus(columnStatus.getStatus()); 를 써야하는지 아니면  existingCard.setStatus(card.getStatus()); 를 써야할지 모르겠어요 Column에서 status enum 불러올겁니다..

        return cardAdapter.save(existingCard);
    }

    // 카드 삭제
    public void deleteCard(Long cardId)
            throws CardNotFoundException/*, UnauthorizedAccessException*/ {
        Card existingCard = cardAdapter.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(ResponseExceptionEnum.CARD_NOT_FOUND));

       /*
       if (!isLoggedInUserAuthorized(existingCard.getUserId())) {
            throw new UnauthorizedAccessException(ResponseExceptionEnum.UNAUTHORIZED_ACCESS);
        }
        */
        cardAdapter.delete(existingCard);
    }

    // 로그인 사용자가 권한이 있는지 확인하는 메서드 ( 인증된 사용자 ID와 로그인사용자가 같아야 함 user랑 합치고 추가!)
    private boolean isLoggedInUserAuthorized(Long userId) {
        // 여기에 실제 사용자 인증 로직 추가해야함
        return true;
    }

}
