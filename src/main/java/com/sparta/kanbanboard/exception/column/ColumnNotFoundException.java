package com.sparta.kanbanboard.exception.column;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class ColumnNotFoundException extends ColumnException{

    public ColumnNotFoundException(
            ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
