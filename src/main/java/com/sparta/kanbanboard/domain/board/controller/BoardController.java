package com.sparta.kanbanboard.domain.board.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping
    public ResponseEntity<HttpResponseDto> createBoard(@RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.BOARD_CREATED,
                boardService.createBoard(requestDto, userDetails.getUser()));
    }

    // 보드 리스트 조회
    @GetMapping
    public ResponseEntity<HttpResponseDto> getBoardList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.BOARD_LIST_RETRIEVED,
                boardService.getBoardList(page - 1, userDetails.getUser()));
    }

    // 보드 수정
    @PatchMapping("/{boardId}")
    public ResponseEntity<HttpResponseDto> updateBoard(@PathVariable("boardId") Long boardId,
            @Valid @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.BOARD_UPDATED,
                boardService.updateBoard(boardId, requestDto, userDetails.getUser()));
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<HttpResponseDto> deleteBoard(@PathVariable("boardId") Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.BOARD_DELETED);
    }

    // 보드에 사용자 초대
    @PostMapping("/{boardId}/invite/{userId}")
    public ResponseEntity<HttpResponseDto> inviteBoard(@PathVariable("boardId") Long boardId,
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        boardService.inviteBoard(boardId, userId, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.USER_INVITED);
    }

}
