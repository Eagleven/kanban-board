package com.sparta.kanbanboard.domain.column.entity;

import lombok.Getter;

@Getter
public enum ColumnStatusEnum {
    UPCOMING("시작 전"),
    IN_PROGRESS("진행 중"),
    DONE("완료");

    private final String columnStatus;

    ColumnStatusEnum(String columnStatus) {
        this.columnStatus = columnStatus;
    }
}
