package com.sparta.kanbanboard.domain.column.entity;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.board.entity.Board;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.column.dto.ColumnRequestDto;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.CascadeType;
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
import lombok.Setter;

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

    @Setter
    @Enumerated(EnumType.STRING)
    @jakarta.persistence.Column(nullable = false)
    private CommonStatusEnum status = CommonStatusEnum.ACTIVE;

    @Setter
    @jakarta.persistence.Column
    private Long prev = null;

    @Setter
    @jakarta.persistence.Column
    private Long next = null;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    private List<Card> cards = new ArrayList<>();

    @Builder
    public Column(Long id, String name, CommonStatusEnum status, List<Card> cards, User user, Board board) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.cards = cards;
        this.user = user;
        this.board = board;
    }

    public Column(String name, User user, Board board) {
        this.name = name;
        this.user = user;
        this.board = board;
    }

    public Column update(ColumnRequestDto requestDto) {
        this.name = requestDto.getName();
        return this;
    }

    public void delete() {
        this.status = CommonStatusEnum.DELETED;
        for (Card card : cards) {
            card.delete();
        }
    }
}