package com.sparta.kanbanboard.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {
    @NotBlank(message = "보드 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "보드 설명을 입력해주세요.")
    private String explanation;
}
