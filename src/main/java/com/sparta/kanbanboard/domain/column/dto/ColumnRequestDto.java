package com.sparta.kanbanboard.domain.column.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ColumnRequestDto {

    @NotBlank(message = "칼럼 이름을 입력해주세요.")
    private String name;
}
