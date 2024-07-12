package com.sparta.kanbanboard.domain.column.service;

import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.column.dto.ColumnResponseDto;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.repository.ColumnRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;

    public ColumnResponseDto create(Long boardId, ColumnRequestDto requestDto) {
        // boardId 가 존재하는지 검사

        // request -> entity
        Column column = Column.toEntity(requestDto);
        columnRepository.save(column);

        // entity -> response
        return ColumnResponseDto.of(column);
    }


    public ColumnResponseDto get(Long columnId) {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new IllegalStateException("칼럼이 존재 하지않습니다.")
        );
        return ColumnResponseDto.of(column);
    }

    public List<ColumnResponseDto> getAll() {
        List<Column> columns = columnRepository.findAll();
        return columns.stream()
                .map(ColumnResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ColumnResponseDto update(Long columnId, ColumnRequestDto requestDto) {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new IllegalStateException("칼럼이 존재 하지않습니다.")
        );

        Column updatedColumn = column.update(requestDto);

        return ColumnResponseDto.of(updatedColumn);
    }

    @Transactional
    public ColumnResponseDto delete(Long columnId) {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new IllegalStateException("칼럼이 존재 하지않습니다.")
        );

        Column deletedColumn = column.delete();

        return ColumnResponseDto.of(deletedColumn);
    }
}
