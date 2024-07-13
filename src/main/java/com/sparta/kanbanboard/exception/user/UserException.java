package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ResponseExceptionEnum responseCodeEnum;

    public UserException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;
    }
}