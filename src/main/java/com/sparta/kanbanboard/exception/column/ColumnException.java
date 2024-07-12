package com.sparta.kanbanboard.exception.column;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class ColumnException extends RuntimeException {
    private final ResponseExceptionEnum responseCodeEnum;

    public ColumnException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;
    }
}
