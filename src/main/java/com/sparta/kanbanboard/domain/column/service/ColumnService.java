package com.sparta.kanbanboard.domain.column.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.dto.ColumnResponseDto;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnAdapter;
import com.sparta.kanbanboard.domain.column.repository.ColumnRepository;
import com.sparta.kanbanboard.exception.column.ColumnException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnAdapter columnAdapter;

    public ColumnResponseDto create(Long boardId, ColumnRequestDto requestDto) {
        // boardId 가 존재하는지 검사

        // request -> entity
        Column column = Column.toEntity(requestDto);
        Column savedColumn = columnAdapter.save(column);

        // entity -> response
        return ColumnResponseDto.of(savedColumn);
    }


    public ColumnResponseDto get(Long columnId) {
        Column column = columnAdapter.findById(columnId);
        return ColumnResponseDto.of(column);
    }

    public List<ColumnResponseDto> getAll() {
        List<Column> columns = columnAdapter.findAll();
        return columns.stream()
                .map(ColumnResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ColumnResponseDto update(Long columnId, ColumnRequestDto requestDto) {
        Column column = columnAdapter.findById(columnId);
        Column updatedColumn = column.update(requestDto);
        return ColumnResponseDto.of(updatedColumn);
    }

    @Transactional
    public ColumnResponseDto delete(Long columnId) {
        Column column = columnAdapter.findById(columnId);
        Column deletedColumn = column.delete();
        return ColumnResponseDto.of(deletedColumn);
    }
}
