package com.sparta.kanbanboard.exception.board;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {
    private final ResponseExceptionEnum responseExceptionEnum;

    public BoardException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}