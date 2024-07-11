package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_DELETED(HttpStatus.UNAUTHORIZED, "탈퇴한 사용자입니다"),

    // Board
    BOARD_CREATED(HttpStatus.CREATED, "보드가 성공적으로 생성되었습니다."),
    BOARD_LIST_RETRIEVED(HttpStatus.OK, "보드 목록을 성공적으로 조회했습니다."),
    BOARD_UPDATED(HttpStatus.OK, "보드 수정이 성공적으로 수행되었습니다."),
    BOARD_DELETED(HttpStatus.OK, "보드가 성공적으로 삭제되었습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}