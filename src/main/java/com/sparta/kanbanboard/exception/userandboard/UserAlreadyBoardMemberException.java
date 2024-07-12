package com.sparta.kanbanboard.exception.userandboard;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class UserAlreadyBoardMemberException  extends UserAndBoardException {

    public UserAlreadyBoardMemberException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
