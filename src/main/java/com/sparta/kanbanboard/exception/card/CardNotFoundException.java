package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class CardNotFoundException extends CardException{
    public CardNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
    // 카드가 존재하지 않는 경우

}
