package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}