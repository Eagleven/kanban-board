package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseExceptionEnum {
    // 익셉션 처리 하는 곳(서비스단에서 유용하게 쓰일 예정)

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_DELETED(HttpStatus.UNAUTHORIZED, "탈퇴한 사용자입니다"),

    // 카드
    INVALID_CARD_DATA(HttpStatus.NOT_FOUND, "카드 필수 데이터가 없습니다."),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "카드를 찾을 수 없습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
