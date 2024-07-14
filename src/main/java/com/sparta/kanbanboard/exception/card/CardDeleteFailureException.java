package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class CardDeleteFailureException extends CardException {

    public CardDeleteFailureException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
