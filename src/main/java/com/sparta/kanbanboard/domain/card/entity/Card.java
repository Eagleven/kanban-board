package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.CommonStatusEnum;
import com.sparta.kanbanboard.common.TimeStampEntity;
import com.sparta.kanbanboard.domain.comment.entity.Comment;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String contents;

    @Column
    private int sequence;

    // 파일 URL 저장
    @Column
    private String attachmentUrl;

    // 마감 기한
    @Column
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommonStatusEnum status = CommonStatusEnum.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private com.sparta.kanbanboard.domain.column.entity.Column column;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Card(String title, String contents, User user,
            com.sparta.kanbanboard.domain.column.entity.Column column, int sequence,
            LocalDateTime dueDate) {
        this.title = title;
        this.contents = contents;
        this.user = user;
        this.column = column;
        this.sequence = sequence;
        this.dueDate = dueDate;
    }

    @Transactional
    public void delete() {
        this.status = CommonStatusEnum.DELETED;
        for (Comment comment : comments) {
            comment.setStatus(CommonStatusEnum.DELETED);
        }
    }
}