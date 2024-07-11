package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.Column;
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
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "cards")
public class Card extends TimeStampEntity {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Column : Card는 1:N 추가해야함

    @Builder
    public Card(String title, String contents, User user/*, Long columnId*/) {
        this.title = title;
        this.contents = contents;
        this.user = user;
        // this.columnId = columnId;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
