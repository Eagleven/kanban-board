package com.sparta.kanbanboard.exception.checkbox;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class CheckBoxNotFoundException extends CheckBoxException{

    public CheckBoxNotFoundException(
            ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
