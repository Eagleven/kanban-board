package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    // 댓글
    CREATE_COMMENT_SUCCESS(HttpStatus.CREATED, "댓글이 작성되었습니다."),
    GET_COMMENTS(HttpStatus.OK, null),
    UPDATE_COMMENT_SUCCESS(HttpStatus.OK, "댓글 수정에 성공하였습니다."),
    DELETE_COMMENT_SUCCESS(HttpStatus.OK, "댓글이 삭제되었습니다."),
    // 유저
    USER_SUCCESS_SIGNUP(HttpStatus.OK, "님의 회원가입을 완료 했습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}