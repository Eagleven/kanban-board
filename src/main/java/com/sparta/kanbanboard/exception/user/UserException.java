package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseCodeEnum;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    private final ResponseCodeEnum responseCodeEnum;

    public UserException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.responseCodeEnum = responseCodeEnum;
    }
}