package com.sparta.kanbanboard.exception.comment;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class UpdateCommentFailureException extends CommentException {
    public UpdateCommentFailureException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
