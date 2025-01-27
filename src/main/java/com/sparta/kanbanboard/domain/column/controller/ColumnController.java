package com.sparta.kanbanboard.domain.column.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.service.ColumnService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/{boardId}/column")
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping
    public ResponseEntity<HttpResponseDto> createColumn(@PathVariable("boardId") Long boardId,
            @RequestBody ColumnRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_CREATED,
                columnService.create(boardId, requestDto, userDetails.getUser()));
    }

    @GetMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> getColumn(@PathVariable("boardId") Long boardId, @PathVariable("columnId") Long columnId) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_RETRIEVED,
                columnService.get(boardId, columnId));
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getAllColumn(@PathVariable("boardId") Long boardId) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_LIST_RETRIEVED,
                columnService.getAll(boardId));
    }

    @PatchMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> updateColumn(@PathVariable("boardId") Long boardId, @PathVariable("columnId") Long columnId,
            @RequestBody ColumnRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_UPDATED,
                columnService.update(boardId, columnId, requestDto, userDetails.getUser()));
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> deleteColumn(@PathVariable("boardId") Long boardId, @PathVariable("columnId") Long columnId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_DELETED,
                columnService.delete(boardId, columnId, userDetails.getUser()));
    }
}