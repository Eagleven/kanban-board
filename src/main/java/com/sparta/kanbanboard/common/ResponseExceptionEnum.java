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
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 완료했습니다."),
    USER_SUCCESS_SIGNUP(HttpStatus.OK, "님의 회원가입을 완료 했습니다."),
    REISSUE_ACCESS_TOKEN(HttpStatus.OK, "억세스 토큰 재발급을 완료했습니다."),

    // board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 보드입니다."),
    BOARD_ALREADY_DELETED(HttpStatus.GONE, "이미 삭제된 보드입니다.")

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
