package com.sparta.kanbanboard.exception.comment;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class CommentNotFoundException extends CommentException {
    public CommentNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
