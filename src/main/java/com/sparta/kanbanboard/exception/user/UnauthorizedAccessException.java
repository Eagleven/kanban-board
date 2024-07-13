package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.exception.card.CardException;

public class UnauthorizedAccessException extends CardException {

    public UnauthorizedAccessException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}