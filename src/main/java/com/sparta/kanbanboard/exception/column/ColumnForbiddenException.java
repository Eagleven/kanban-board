package com.sparta.kanbanboard.exception.column;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class ColumnForbiddenException extends ColumnException{

    public ColumnForbiddenException(
            ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
