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
    NOT_FOUND_AUTHENTICATION_INFO(HttpStatus.BAD_REQUEST, "사용자 정보가 일치하지 않습니다. 다시 시도해 주세요 :)" ),
    INVALID_REFRESHTOKEN(HttpStatus.NOT_FOUND, "유효하지 않은 리프레쉬 토큰입니다."),
    FAIL_TO_CHANGE_ROLE(HttpStatus.BAD_REQUEST, "Role 변경을 실패했습니다." ),

    // board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 보드입니다."),
    BOARD_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 보드입니다."),
    FORBIDDEN_CREATE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 생성할 수 있습니다."),
    FORBIDDEN_UPDATE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 수정할 수 있습니다."),
    FORBIDDEN_DELETE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 삭제할 수 있습니다."),
    FORBIDDEN_INVITE_BOARD(HttpStatus.FORBIDDEN, "보드는 manager 사용자만 초대할 수 있습니다."),
    USER_NOT_BOARD_MEMBER(HttpStatus.FORBIDDEN, "보드의 멤버가 아닙니다."),
    USER_ALREADY_BOARD_MEMBER(HttpStatus.FORBIDDEN, "이미 보드에 초대된 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
