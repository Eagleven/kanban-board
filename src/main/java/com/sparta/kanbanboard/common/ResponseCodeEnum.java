package com.sparta.kanbanboard.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    // 성공 처리 하는 곳(Controller에서 쓰일 예정)

    // 카드
    CARD_CREATE_SUCCESS(HttpStatus.CREATED, "카드 생성을 성공하셨습니다."),
    CARD_GET_ALL_SUCCESS(HttpStatus.OK, "모든 카드 조회에 성공하셨습니다."),
    CARD_GET_USER_SUCCESS(HttpStatus.OK, "작업자별 카드 조회에 성공하셨습니다."),
    CARD_GET_STATUS_SUCCESS(HttpStatus.OK, "상태별 카드 조회에 성공하셨습니다."),
    CARD_UPDATE_SUCCESS(HttpStatus.OK, "카드 업데이트에 성공하셨습니다."),
    CARD_DELETE_SUCCESS(HttpStatus.OK, "카드 삭제에 성공하셨습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}