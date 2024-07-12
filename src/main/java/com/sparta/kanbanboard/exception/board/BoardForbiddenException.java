package com.sparta.kanbanboard.exception.board;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.exception.userandboard.UserAndBoardException;

public class BoardForbiddenException extends BoardException {
    public BoardForbiddenException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}