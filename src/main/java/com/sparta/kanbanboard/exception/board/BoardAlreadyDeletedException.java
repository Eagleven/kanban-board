package com.sparta.kanbanboard.exception.board;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class BoardAlreadyDeletedException extends BoardException{
    public BoardAlreadyDeletedException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
