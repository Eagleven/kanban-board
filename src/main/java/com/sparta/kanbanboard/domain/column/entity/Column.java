package com.sparta.kanbanboard.domain.column.entity;

import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "columns")
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private ColumnStatus status;

    private int sequence;

    private LocalDate createdAt;

    private LocalDate modifiedAt;

    @Builder
    public Column(Long id, String name, ColumnStatus status, LocalDate createdAt,
            LocalDate modifiedAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Builder
    public static Column toEntity(ColumnRequestDto requestDto) {
        return Column.builder()
                .name(requestDto.getName())
                .status(ColumnStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .modifiedAt(LocalDate.now())
                .build();
    }

    public Column update(ColumnRequestDto requestDto) {
        this.name = requestDto.getName();
        this.modifiedAt = LocalDate.now();
        return this;
    }

    public Column delete() {
        this.status = ColumnStatus.DELETED;
        return this;
    }

}
