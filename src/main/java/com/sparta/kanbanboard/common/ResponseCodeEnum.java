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
    // 카드
    CARD_CREATE_SUCCESS(HttpStatus.CREATED, "카드 생성을 성공하셨습니다."),
    CARD_GET_ALL_SUCCESS(HttpStatus.OK, "모든 카드 조회에 성공하셨습니다."),
    CARD_GET_USER_SUCCESS(HttpStatus.OK, "작업자별 카드 조회에 성공하셨습니다."),
    CARD_GET_STATUS_SUCCESS(HttpStatus.OK, "상태별 카드 조회에 성공하셨습니다."),
    CARD_UPDATE_SUCCESS(HttpStatus.OK, "카드 업데이트에 성공하셨습니다."),
    CARD_DELETE_SUCCESS(HttpStatus.OK, "카드 삭제에 성공하셨습니다.");
    REISSUE_ACCESS_TOKEN(HttpStatus.OK, "억세스 토큰 재발급을 완료했습니다."),
    SUCCESS_SUBSCRIPTION(HttpStatus.OK, "로 회원 등급이 변경되었습니다."),
    SUCCESS_GET_USERS(HttpStatus.OK, "유저 목록을 조회했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}