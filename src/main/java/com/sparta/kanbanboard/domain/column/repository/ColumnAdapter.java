package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.domain.column.entity.ColumnStatus;
import com.sparta.kanbanboard.exception.column.ColumnAlreadyDeletedException;
import com.sparta.kanbanboard.exception.column.ColumnException;
import com.sparta.kanbanboard.exception.column.ColumnNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ColumnAdapter {

    private final ColumnRepository columnRepository;

    public Column save(Column column) {
        return columnRepository.save(column);
    }

    public Column findById(Long columnId) {
        Column column = columnRepository.findById(columnId).orElseThrow(
                () -> new ColumnNotFoundException(ResponseExceptionEnum.COLUMN_NOT_FOUND)
        );

        if (isColumnDeleted(column)) {
            throw new ColumnAlreadyDeletedException(ResponseExceptionEnum.DELETED_COLUMN);
        }

        return column;
    }

    public List<Column> findAll() {
        return columnRepository.findAll().stream()
                .filter(column -> column.getStatus().equals(ColumnStatus.ACTIVE))
                .collect(Collectors.toList());
    }

    public boolean isColumnDeleted (Column column) {
        return column.getStatus().equals(ColumnStatus.DELETED);
    }
}
