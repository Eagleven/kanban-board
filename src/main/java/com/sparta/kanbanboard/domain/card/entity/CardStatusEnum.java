package com.sparta.kanbanboard.domain.card.entity;

import lombok.Getter;

@Getter
public enum CardStatusEnum {
    UPCOMING("시작 전"),
    IN_PROGRESS("진행 중"),
    DONE("완료"),
    ;


    private final String cardStatus;

    CardStatusEnum(String cardStatus) {this.cardStatus = cardStatus;}
}
