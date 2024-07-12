package com.sparta.kanbanboard.domain.column.repository;

import com.sparta.kanbanboard.domain.column.entity.Column;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Column, Long> {
}
