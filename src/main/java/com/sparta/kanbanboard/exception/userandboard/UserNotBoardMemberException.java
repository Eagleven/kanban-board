package com.sparta.kanbanboard.exception.userandboard;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.exception.user.UserException;

public class UserNotBoardMemberException extends UserAndBoardException {

    public UserNotBoardMemberException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
