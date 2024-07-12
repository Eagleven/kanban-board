package com.sparta.kanbanboard.domain.column.dto;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.domain.column.entity.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ColumnResponseDto {

    private String name;

    private CommonStatusEnum status;

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
