package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class UserDuplicatedException extends UserException {
    public UserDuplicatedException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}