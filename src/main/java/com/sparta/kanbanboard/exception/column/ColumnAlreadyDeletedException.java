package com.sparta.kanbanboard.exception.column;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class ColumnAlreadyDeletedException extends ColumnException{

    public ColumnAlreadyDeletedException(
            ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
