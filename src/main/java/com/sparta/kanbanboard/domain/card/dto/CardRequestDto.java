package com.sparta.kanbanboard.domain.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardRequestDto {
    public String title;
    public String contents;

}
