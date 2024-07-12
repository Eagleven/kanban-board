package com.sparta.kanbanboard.domain.column.entity;

import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "columns")
public class Column extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.persistence.Column(nullable = false)
    private String name;

    @jakarta.persistence.Column(nullable = false)
    private ColumnStatus status;

    private int sequence;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Column(Long id, String name, ColumnStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    @Builder
    public static Column toEntity(ColumnRequestDto requestDto) {
        return Column.builder()
                .name(requestDto.getName())
                .status(ColumnStatus.ACTIVE)
                .build();
    }

    public Column update(ColumnRequestDto requestDto) {
        this.name = requestDto.getName();
        return this;
    }

    public Column delete() {
        this.status = ColumnStatus.DELETED;
        return this;
    }

}
