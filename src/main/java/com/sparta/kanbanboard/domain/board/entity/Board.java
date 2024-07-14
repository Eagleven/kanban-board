package com.sparta.kanbanboard.domain.board.entity;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.board.dto.BoardRequestDto;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Board extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String explanation;

    @Column
    @Setter
    @Enumerated(EnumType.STRING)
    private CommonStatusEnum status;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<com.sparta.kanbanboard.domain.column.entity.Column> columns = new ArrayList<>();

    public Board(String name, String explanation, User user) {
        this.name = name;
        this.explanation = explanation;
        this.status = CommonStatusEnum.ACTIVE;
        this.user = user;
    }

    public void update(BoardRequestDto requestDto) {
        this.name = requestDto.getName();
        this.explanation = requestDto.getExplanation();
    }

    public void delete(Board board){
        this.status = CommonStatusEnum.DELETED;
        for (com.sparta.kanbanboard.domain.column.entity.Column column : board.getColumns()) {
            column.delete();
        }
    }
}
