package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseExceptionEnum {
    // 유저
    REFRESH_TOKEN_UNAVAILABLE(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레쉬토큰 입니다."),
    USER_FAIL_SIGNUP(HttpStatus.BAD_REQUEST, "회원가입에 실패했습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_DELETED(HttpStatus.UNAUTHORIZED, "탈퇴한 사용자입니다"),
    NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다. 다시 시도해 주세요 :)"),
    INVALID_REFRESHTOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 리프레쉬 토큰입니다."),
    FAIL_TO_CHANGE_ROLE(HttpStatus.BAD_REQUEST, "Role 변경을 실패했습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),

    // board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 보드입니다."),
    BOARD_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 보드입니다."),
    FORBIDDEN_CREATE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 생성할 수 있습니다."),
    FORBIDDEN_UPDATE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 수정할 수 있습니다."),
    FORBIDDEN_DELETE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 삭제할 수 있습니다."),
    FORBIDDEN_INVITE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 초대할 수 있습니다."),
    USER_NOT_BOARD_MEMBER(HttpStatus.FORBIDDEN, "보드의 멤버가 아닙니다."),
    USER_ALREADY_BOARD_MEMBER(HttpStatus.FORBIDDEN, "이미 보드에 초대된 사용자입니다."),

    // column
    COLUMN_NOT_FOUND(HttpStatus.NOT_FOUND, "칼럼을 찾을 수 없습니다"),
    COLUMN_ALREADY_DELETE(HttpStatus.BAD_REQUEST, "삭제된 칼럼 입니다."),
    FORBIDDEN_CREATE_COLUMN(HttpStatus.FORBIDDEN, "칼럼은 manager 사용자만 생성할 수 있습니다."),
    FORBIDDEN_UPDATE_COLUMN(HttpStatus.FORBIDDEN, "칼럼은 manager 사용자만 수정할 수 있습니다."),

    // card
    INVALID_CARD_DATA(HttpStatus.NOT_FOUND, "카드 필수 데이터가 없습니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다."),
    CARD_UPDATE_FAILURE(HttpStatus.BAD_REQUEST, "카드를 수정할 수 없습니다."),
    CARD_DELETE_FAILURE(HttpStatus.BAD_REQUEST, "카드를 삭제할 수 없습니다."),

    // 댓글
    CREATE_COMMENT_FAILURE(HttpStatus.BAD_REQUEST, "댓글 작성에 실패하였습니다."),
    DELETE_COMMENT_FAILURE(HttpStatus.BAD_REQUEST, "해당 댓글을 삭제할 수 없습니다"),
    UPDATE_COMMENT_FAILURE(HttpStatus.BAD_REQUEST, "댓글을 수정할 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),
    COMMENT_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "댓글을 입력하세요."),

    // 체크박스
    CHECKBOX_NOT_FOUND(HttpStatus.BAD_REQUEST, "체크박스를 찾을 수 없습니다."),
    UPDATE_CHECKBOX_FAILURE(HttpStatus.BAD_REQUEST, "체크박스 생성이 실패하였습니다."),
    DELETE_CHECKBOX_FAILURE(HttpStatus.BAD_REQUEST, "체크박스 생성이 실패하였습니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
