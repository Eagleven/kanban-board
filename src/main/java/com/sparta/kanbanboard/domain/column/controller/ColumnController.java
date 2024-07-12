package com.sparta.kanbanboard.domain.column.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/column")
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping("/{boardId}")
    public ResponseEntity<HttpResponseDto> createColumn(@PathVariable Long boardId,
            @RequestBody ColumnRequestDto requestDto) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_CREATED,
                columnService.create(boardId, requestDto));
    }

    @GetMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> getColumn(@PathVariable Long columnId) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_RETRIEVED,
                columnService.get(columnId));
    }

    @GetMapping()
    public ResponseEntity<HttpResponseDto> getAllColumn() {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_LIST_RETRIEVED,
                columnService.getAll());
    }

    @PutMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> updateColumn(@PathVariable Long columnId,
            @RequestBody ColumnRequestDto requestDto) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_UPDATED,
                columnService.update(columnId, requestDto));
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<HttpResponseDto> deleteColumn(@PathVariable Long columnId) {
        return ResponseUtils.of(ResponseCodeEnum.COLUMN_UPDATED,
                columnService.delete(columnId));
    }
}
