package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseExceptionEnum {
    // common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_DELETED(HttpStatus.UNAUTHORIZED, "탈퇴한 사용자입니다"),

    // board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 보드입니다."),
    BOARD_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 보드입니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
