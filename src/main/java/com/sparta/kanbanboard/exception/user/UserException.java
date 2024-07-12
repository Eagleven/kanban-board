package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ResponseExceptionEnum responseExceptionEnum;

    public UserException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }

}
