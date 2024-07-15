package com.sparta.kanbanboard.domain.checkbox.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.checkbox.entity.CheckBox;
import com.sparta.kanbanboard.exception.checkbox.CheckBoxNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckBoxAdapter {

    private final CheckBoxRepository checkBoxRepository;

    public CheckBox save(CheckBox checkBox) {
        return checkBoxRepository.save(checkBox);
    }

    public CheckBox findById(Long checkboxId) {
        return checkBoxRepository.findById(checkboxId).orElseThrow(
                () -> new CheckBoxNotFoundException(ResponseExceptionEnum.CHECKBOX_NOT_FOUND)
        );
    }

    public void delete(CheckBox checkBox) {
        checkBoxRepository.delete(checkBox);
    }
}
