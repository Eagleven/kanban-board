package com.sparta.kanbanboard.exception.board;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class BoardNotFoundException extends BoardException{
    public BoardNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
