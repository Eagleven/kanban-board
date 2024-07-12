package com.sparta.kanbanboard.exception.column;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class ColumnException extends RuntimeException {
    private final ResponseExceptionEnum responseExceptionEnum;

    public ColumnException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
