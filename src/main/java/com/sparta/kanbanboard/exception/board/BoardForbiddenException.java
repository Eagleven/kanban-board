package com.sparta.kanbanboard.exception.board;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class BoardForbiddenException extends BoardException {
    public BoardForbiddenException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}