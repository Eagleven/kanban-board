package com.sparta.kanbanboard.domain.column.dto;

import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.entity.ColumnStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ColumnResponseDto {

    private String name;

    private ColumnStatus status;

    public ColumnResponseDto(Column column) {
        this.name = column.getName();
        this.status = column.getStatus();
    }

    public static ColumnResponseDto of(Column column) {
        return ColumnResponseDto.builder()
                .name(column.getName())
                .status(column.getStatus())
                .build();
    }
}
