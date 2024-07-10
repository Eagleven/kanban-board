package com.sparta.kanbanboard.domain.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardResponseDto {
    public Long id;
    public String title;
    public String contents;

    public CardResponseDto(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

}
