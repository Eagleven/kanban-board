package com.sparta.kanbanboard.exception.card;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class ColumnNotFoundException extends CardException {

    public ColumnNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
