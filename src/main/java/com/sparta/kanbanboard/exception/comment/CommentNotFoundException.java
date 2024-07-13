package com.sparta.kanbanboard.exception.comment;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.exception.card.CardException;

public class CommentNotFoundException extends CardException {
    public CommentNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}