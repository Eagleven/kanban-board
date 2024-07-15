package com.sparta.kanbanboard.domain.checkbox.entity;

import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.checkbox.dto.CheckBoxRequestDto;
import com.sparta.kanbanboard.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "checkbox")
public class CheckBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_checked")
    private boolean isChecked;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public CheckBox(boolean isChecked, String text, Card card, User user) {
        this.isChecked = isChecked;
        this.text = text;
        this.card = card;
        this.user = user;
    }

    public CheckBox update(CheckBoxRequestDto requestDto) {
        this.text = requestDto.getText();
        return this;
    }

    public CheckBox checkToggle(CheckBox checkBox) {
        this.isChecked = !checkBox.isChecked;
        return this;
    }
}
