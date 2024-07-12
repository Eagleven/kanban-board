package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class UnauthorizedAccessException extends CardException {

    public UnauthorizedAccessException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }


}
