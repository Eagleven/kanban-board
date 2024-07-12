package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    // 유저
    SUCCESS_LOGIN(HttpStatus.OK, "로그인을 완료했습니다."),
    USER_SUCCESS_SIGNUP(HttpStatus.OK, "님의 회원가입을 완료 했습니다."),
    REISSUE_ACCESS_TOKEN(HttpStatus.OK, "억세스 토큰 재발급을 완료했습니다."),

    SUCCESS_SUBSCRIPTION(HttpStatus.OK, "회원 등급이 변경되었습니다."),
    SUCCESS_GET_USERS(HttpStatus.OK, "유저 목록을 조회했습니다."),
    SUCCESS_GET_USER(HttpStatus.OK, "유저 조회 완료했습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃을 완료했습니다.");

    // Board
    BOARD_CREATED(HttpStatus.CREATED, "보드가 성공적으로 생성되었습니다."),
    BOARD_LIST_RETRIEVED(HttpStatus.OK, "보드 목록을 성공적으로 조회했습니다."),
    BOARD_UPDATED(HttpStatus.OK, "보드 수정이 성공적으로 수행되었습니다."),
    BOARD_DELETED(HttpStatus.OK, "보드가 성공적으로 삭제되었습니다."),

    // Column
    COLUMN_CREATED(HttpStatus.CREATED, "칼럼이 성공적으로 생성되었습니다."),
    COLUMN_RETRIEVED(HttpStatus.OK, "칼럼을 성공적으로 조회했습니다."),
    COLUMN_LIST_RETRIEVED(HttpStatus.OK, "칼럼 목록을 성공적으로 조회했습니다."),
    COLUMN_UPDATED(HttpStatus.OK, "칼럼 수정이 성공적으로 수행되었습니다."),
    COLUMN_DELETED(HttpStatus.OK, "칼럼이 성공적으로 삭제되었습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}