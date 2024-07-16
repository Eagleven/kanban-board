package com.sparta.kanbanboard.domain.card.service;

import com.sparta.kanbanboard.aws.service.S3UploadService;
import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.dto.CardRequestDto;
import com.sparta.kanbanboard.domain.card.dto.CardResponseDto;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.domain.card.repository.CardRepository;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.card.CardUpdateFailureException;
import com.sparta.kanbanboard.exception.user.UnauthorizedAccessException;
import com.sparta.kanbanboard.exception.userandboard.UserNotBoardMemberException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardAdapter cardAdapter;
    private final ColumnAdapter columnAdapter;
    private final CardRepository cardRepository;
    private final UserAndBoardAdapter userAndBoardAdapter;
    private final S3UploadService s3UploadService;

    // 모든 카드 조회
    @Transactional(readOnly = true)
    public List<CardResponseDto> getAllCards() {
        return cardAdapter.findByStatus(CommonStatusEnum.ACTIVE).stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 상태별 카드 조회
    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardsByColumn(Long columnId) {
        Column column = columnAdapter.findById(columnId);
        return cardRepository.findByColumnAndStatus(column, CommonStatusEnum.ACTIVE).stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 사용자별 카드 조회
    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardByUser(Long userId) {
        return cardAdapter.findActiveCardsByUserId(userId).stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());
    }

    // 카드 생성
    @Transactional
    public CardResponseDto createCard(Long columnId, CardRequestDto cardRequestDto, User user)
            throws IOException {
        checkUserAuthentication();
        Column column = columnAdapter.findById(columnId);
        if (!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(),
                column.getBoard().getId())) {
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }
        String fileUrl = null;
        if (cardRequestDto.getFile() != null && !cardRequestDto.getFile().isEmpty()) {
            fileUrl = s3UploadService.saveFile(cardRequestDto.getFile());
        }

        Card card = Card.builder()
                .title(cardRequestDto.getTitle())
                .contents(cardRequestDto.getContents())
                .column(column)
                .sequence(0) // 초기 순서 설정
                .user(user) // 사용자 설정
                .fileUrl(fileUrl)
                .build();

        Card savedCard = cardAdapter.save(card);
        List<Card> cards = column.getCards();

        cards.add(savedCard);
        columnAdapter.save(column);

        return new CardResponseDto(savedCard);
    }

    // 카드 수정
    @Transactional
    public CardResponseDto updateCard(Long cardId, CardRequestDto cardRequestDto, User user)
            throws IOException {
        checkUserAuthentication();
        Card card = cardAdapter.findById(cardId);
        if (!user.getUsername().equals(card.getUser().getUsername()) && !user.getUserRole()
                .equals(Role.MANAGER)) {
            throw new CardUpdateFailureException(ResponseExceptionEnum.CARD_UPDATE_FAILURE);
        }
        if (cardRequestDto.getFile() != null && !cardRequestDto.getFile().isEmpty()) {
            if (card.getFileUrl() != null) {
                s3UploadService.deleteFile(card.getFileUrl());
            }
            String newFileUrl = s3UploadService.saveFile(cardRequestDto.getFile());
            card.setFileUrl(newFileUrl);
        }
        return new CardResponseDto(cardAdapter.update(card, cardRequestDto));
    }

    // 파일 다운로드를 위한 메서드
    public ResponseEntity<UrlResource> downloadFile(Long cardId) {
        Card card = cardAdapter.findById(cardId);
        if (card.getFileUrl() == null) {
            throw new RuntimeException("File not found");
        }
        // S3UploadService의 downloadImage 메서드를 사용합니다
        return s3UploadService.downloadImage(extractFilenameFromUrl(card.getFileUrl()));
    }

    private String extractFilenameFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            String filename = path.substring(path.lastIndexOf('/') + 1);
            // URL 디코딩을 수행하여 특수 문자 처리
            return URLDecoder.decode(filename, StandardCharsets.UTF_8.toString());
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            log.error("Error extracting filename from URL: " + fileUrl, e);
            throw new IllegalArgumentException("Invalid file URL", e);
        }
    }

//    // 카드 위치 수정
//    @Transactional
//    public void updateCardOrder(Long cardId, Long newColumnId, int newSequence) {
//        checkUserAuthentication();
//        Column newColumn = columnAdapter.findById(newColumnId);
//        cardAdapter.updateCardOrder(cardId, newColumnId, newSequence);
//    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId, User user) {
        checkUserAuthentication();
        Card card = cardAdapter.findById(cardId);
        if (!user.getUsername().equals(card.getUser().getUsername()) && !user.getUserRole()
                .equals(Role.MANAGER)) {
            throw new CardUpdateFailureException(ResponseExceptionEnum.CARD_DELETE_FAILURE);
        }
        cardAdapter.softDelete(card);
    }

    private void checkUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException(
                    ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO);
        }
    }

    public CardResponseDto getCardById(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        return new CardResponseDto(card);
    }


}