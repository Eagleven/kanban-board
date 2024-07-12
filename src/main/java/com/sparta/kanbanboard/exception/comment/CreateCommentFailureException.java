package com.sparta.kanbanboard.exception.comment;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class CreateCommentFailureException extends CommentException {
    public CreateCommentFailureException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
