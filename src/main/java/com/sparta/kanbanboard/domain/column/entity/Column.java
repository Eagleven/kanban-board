package com.sparta.kanbanboard.domain.column.entity;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    // 시작 전, 진행 중, 완료
    @jakarta.persistence.Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(nullable = false)
    private CommonStatusEnum status = CommonStatusEnum.ACTIVE;

    private int sequence;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "column")
    private List<Card> cards = new ArrayList<>();

    @Builder
    public Column(Long id, String name, CommonStatusEnum status, int sequence, List<Card> cards, User user) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.sequence = sequence;
        this.cards = cards;
        this.user = user;
    }

    // static 에 @Builder 를 쓰려면 모든 필드를 가진 생성자가 존재해야한다
    @Builder
    public static Column toEntity(ColumnRequestDto requestDto, int sequence, User user) {
        return Column.builder()
                .name(requestDto.getName())
                .status(CommonStatusEnum.ACTIVE)
                .sequence(sequence)
                .user(user)
                .build();
    }

    public Column updateSequence(int sequence) {
        this.sequence = sequence;
        return this;
    }

    public Column update(ColumnRequestDto requestDto) {
        this.name = requestDto.getName();
        return this;
    }

    public Column delete() {
        this.status = CommonStatusEnum.DELETED;
        return this;
    }
}