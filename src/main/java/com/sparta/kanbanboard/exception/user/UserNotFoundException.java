package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseCodeEnum;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}