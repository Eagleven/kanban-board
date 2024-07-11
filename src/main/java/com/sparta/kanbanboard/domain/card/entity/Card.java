package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.TimeStampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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


//    @OneToMany(mappedBy = "card")
//    private User user;
//
//    @ManyToOne    =====> 여기서 column이랑 연관관계 맺고 나서 status enum 불러와서 수정 처리 하기
//    @JoinColumn(name = "column_id")
//    private Column column;


    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }




}
