package com.sparta.kanbanboard.domain.column.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.dto.ColumnResponseDto;
import com.sparta.kanbanboard.domain.column.service.ColumnService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public HttpResponseDto createColumn(@PathVariable Long boardId,
            @RequestBody ColumnRequestDto requestDto) {
        ColumnResponseDto responseDto = columnService.create(boardId, requestDto);
        return new HttpResponseDto(HttpStatus.OK, "칼럼 생성이 완료되었습니다.", responseDto);
    }

    @GetMapping("{calumnId}")
    public HttpResponseDto getColumn(@PathVariable Long calumnId) {
        ColumnResponseDto responseDto = columnService.get(calumnId);
        return new HttpResponseDto(HttpStatus.OK, "칼럼 조회가 완료되었습니다.", responseDto);
    }

    @GetMapping()
    public HttpResponseDto getAllColumn() {
        List<ColumnResponseDto> responses = columnService.getAll();
        return new HttpResponseDto(HttpStatus.OK, "칼럼 조회가 완료되었습니다.", responses);
    }

    @PutMapping("{columnId}")
    public HttpResponseDto updateColumn(@PathVariable Long columnId,
            @RequestBody ColumnRequestDto requestDto) {
        ColumnResponseDto responseDto = columnService.update(columnId, requestDto);
        return new HttpResponseDto(HttpStatus.OK, "칼럼 수정이 완료되었습니다.", responseDto);
    }

    @DeleteMapping("{columnId}")
    public HttpResponseDto deleteColumn(@PathVariable Long columnId) {
        ColumnResponseDto responseDto = columnService.delete(columnId);
        return new HttpResponseDto(HttpStatus.OK, "칼럼 삭제가 완료되었습니다.", responseDto);
    }
}
