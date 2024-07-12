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

    // 카드
    INVALID_CARD_DATA(HttpStatus.NOT_FOUND, "카드 필수 데이터가 없습니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
