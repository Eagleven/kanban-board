package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseCodeEnum;
import lombok.Getter;

@Getter
public class CardException extends RuntimeException {
    private final ResponseCodeEnum responseCodeEnum;

    public CardException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;
    }

}
