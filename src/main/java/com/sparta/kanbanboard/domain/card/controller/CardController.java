package com.sparta.kanbanboard.domain.card.controller;

import com.sparta.kanbanboard.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

}
