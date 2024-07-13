package com.sparta.kanbanboard.domain.card.dto;

import com.sparta.kanbanboard.domain.card.entity.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardResponseDto {

    private Long id;
    private String title;
    private String contents;
    private int sequence;

    public CardResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.contents = card.getContents();
        this.sequence = card.getSequence();
    }
}
