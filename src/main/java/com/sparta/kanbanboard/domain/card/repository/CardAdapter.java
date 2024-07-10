package com.sparta.kanbanboard.domain.card.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CardAdapter {
private final CardRepository cardRepository;
}
