package com.sparta.kanbanboard.domain.checkbox.repository;

import com.sparta.kanbanboard.domain.checkbox.entity.CheckBox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckBoxRepository extends JpaRepository<CheckBox, Long> {
}
