package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.dto.CardResponseDto;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private final CardAdapter cardAdapter;

}
