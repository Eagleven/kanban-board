package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.TimeStampEntity;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    @Column
    private int position;

    private Long assigneeId;

    // 마감 기한
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonStatusEnum status = CommonStatusEnum.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Column : Card는 1:N 추가해야함
    // @OneToMany

    @Builder
    public Card(String title, String contents, User user, /* Long columnId,*/ int position,
            LocalDateTime dueDate) {
        this.title = title;
        this.contents = contents;
        this.user = user;
        /*
        this.columnId = columnId;
         */
        this.position = position;
        this.dueDate = dueDate;
    }

    public void update(String title, String contents, /* Long columnId,*/ int position,
            LocalDateTime dueDate) {
        this.title = title;
        this.contents = contents;
        /*
        this.columnId = columnId;
        */
        this.position = position;
        this.dueDate = dueDate;
    }

    public void delete() {
        this.status = CommonStatusEnum.DELETED;
    }
}
