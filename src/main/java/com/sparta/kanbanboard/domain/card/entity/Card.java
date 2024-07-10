package com.sparta.kanbanboard.domain.card.entity;

import com.sparta.kanbanboard.common.TimeStampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "cardId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

//    @OneToMany(mappedBy = "card")
//    private Long userId;
//
//    @OneToMany(mappedBy = "card")
//    private Long columnId;


    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }




}
