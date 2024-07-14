package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CardException extends RuntimeException {
    private final ResponseExceptionEnum responseExceptionEnum;

    public CardException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }

}
