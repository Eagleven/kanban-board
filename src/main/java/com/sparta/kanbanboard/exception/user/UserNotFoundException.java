package com.sparta.kanbanboard.exception.user;

import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.exception.card.UserException;

public class UserNotFoundException extends UserException {
    public UserNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}