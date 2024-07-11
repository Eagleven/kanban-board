package com.sparta.kanbanboard.domain.board.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createBoard(@RequestBody BoardRequestDto requestDto) {
        return ResponseUtils.of(ResponseCodeEnum.BOARD_CREATED,
                boardService.createBoard(requestDto));
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getBoardList() {
        return ResponseUtils.of(ResponseCodeEnum.BOARD_LIST_RETRIEVED, boardService.getBoardList());
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<HttpResponseDto> updateBoard(@PathVariable("boardId") Long boardId,
            @Valid @RequestBody BoardRequestDto requestDto) {
        return ResponseUtils.of(ResponseCodeEnum.BOARD_UPDATED, boardService.updateBoard(boardId, requestDto));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<HttpResponseDto> deleteBoard(@PathVariable("boardId") Long boardId){
        boardService.deleteBoard(boardId);
        return ResponseUtils.of(ResponseCodeEnum.BOARD_DELETED);
    }

}
