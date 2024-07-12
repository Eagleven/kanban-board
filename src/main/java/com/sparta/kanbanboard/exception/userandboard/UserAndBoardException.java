package com.sparta.kanbanboard.exception.userandboard;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class UserAndBoardException extends RuntimeException {
    private final ResponseExceptionEnum responseExceptionEnum;

    public UserAndBoardException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}