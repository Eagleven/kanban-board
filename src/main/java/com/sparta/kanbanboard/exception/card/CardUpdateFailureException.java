package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class CardUpdateFailureException extends CardException {

    public CardUpdateFailureException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
