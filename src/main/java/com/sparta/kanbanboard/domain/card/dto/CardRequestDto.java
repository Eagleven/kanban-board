package com.sparta.kanbanboard.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardRequestDto {

    @NotBlank(message = "카드 제목을 입력해주세요.")
    public String title;

    @NotBlank(message = "카드 내용을 입력해주세요.")
    public String contents;

    private Long userId;

    private Long columnId;

    private LocalDateTime dueDate;


    public void setUserId(Long user) {
        this.userId = user;
    }

    public void setColumnId(Long column) {
        this.columnId = column;
    }
}
