package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.column.entity.Column;
import com.sparta.kanbanboard.exception.column.ColumnAlreadyDeletedException;
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

    public boolean existById(Long columnId) {
        return columnRepository.existsById(columnId);
    }

    public Column findById(Long columnId) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(ResponseExceptionEnum.COLUMN_NOT_FOUND)
        );

        if (isColumnDeleted(column)) {
            throw new ColumnAlreadyDeletedException(ResponseExceptionEnum.COLUMN_ALREADY_DELETE);
        }
        return column;
    }

    public boolean isColumnDeleted(Column column) {
        return column.getStatus().equals(CommonStatusEnum.DELETED);
    }

    public void deleteColumn(Column column) {
        column.delete();
        columnRepository.save(column);
    }
}