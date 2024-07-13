package com.sparta.kanbanboard.exception.comment;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;

public class DeleteCommentFailureException extends CommentException {
    public DeleteCommentFailureException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }

}
