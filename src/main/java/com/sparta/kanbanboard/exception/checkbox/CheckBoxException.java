package com.sparta.kanbanboard.exception.checkbox;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CheckBoxException extends RuntimeException {
    private final ResponseExceptionEnum responseExceptionEnum;

    public CheckBoxException(ResponseExceptionEnum responseExceptionEnum) {
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
