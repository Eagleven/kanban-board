package com.sparta.kanbanboard.domain.comment.entity;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.comment.dto.CommentDto;

import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false)
    private String content;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommonStatusEnum status = CommonStatusEnum.ACTIVE;

    public Comment(User user, Card card, CommentDto commentDto) {
        this.user = user;
        this.card = card;
        this.content = commentDto.getContent();
    }

    public void updateComment(CommentDto commentDto){
        this.content = commentDto.getContent();
    }
}
