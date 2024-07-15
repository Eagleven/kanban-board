package com.sparta.kanbanboard.domain.checkbox.dto;

import com.sparta.kanbanboard.domain.checkbox.entity.CheckBox;
import lombok.Getter;

@Getter
public class CheckBoxResponseDto {

    private boolean isChecked;

    private String text;

    public CheckBoxResponseDto(CheckBox savedCheckBox) {
        this.isChecked = savedCheckBox.isChecked();
        this.text = savedCheckBox.getText();
    }
}
