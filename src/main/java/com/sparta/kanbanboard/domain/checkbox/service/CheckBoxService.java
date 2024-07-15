package com.sparta.kanbanboard.domain.checkbox.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.card.entity.Card;
import com.sparta.kanbanboard.domain.card.repository.CardAdapter;
import com.sparta.kanbanboard.domain.checkbox.dto.CheckBoxRequestDto;
import com.sparta.kanbanboard.domain.checkbox.dto.CheckBoxResponseDto;
import com.sparta.kanbanboard.domain.checkbox.entity.CheckBox;
import com.sparta.kanbanboard.domain.checkbox.repository.CheckBoxAdapter;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.userandboard.repository.UserAndBoardAdapter;
import com.sparta.kanbanboard.exception.userandboard.UserNotBoardMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckBoxService {

    private final CheckBoxAdapter checkBoxAdapter;
    private final CardAdapter cardAdapter;
    private final UserAndBoardAdapter userAndBoardAdapter;

    public CheckBoxResponseDto createCheckBox(Long card_id, CheckBoxRequestDto requestDto, User user) {
        // 카드 id 확인
        Card card = cardAdapter.findById(card_id);

        // user id 확인
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), card.getColumn().getBoard().getId())){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 체크박스 생성
        CheckBox checkBox = CheckBox.builder()
                .isChecked(true)
                .text(requestDto.getText())
                .card(card)
                .user(user)
                .build();

        // 체크박스 저장
        CheckBox savedCheckBox = checkBoxAdapter.save(checkBox);
        return new CheckBoxResponseDto(savedCheckBox);
    }

    @Transactional
    public CheckBoxResponseDto updateCheckBox(Long card_id, Long checkbox_id, CheckBoxRequestDto requestDto, User user) {
        // 카드 id 확인
        Card card = cardAdapter.findById(card_id);

        // user id 확인
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), card.getColumn().getBoard().getId())){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 체크박스 id 확인
        CheckBox checkBox = checkBoxAdapter.findById(checkbox_id);

        // 체크박스 업데이트
        CheckBox updatedCheckBox = checkBox.update(requestDto);

        return new CheckBoxResponseDto(updatedCheckBox);
    }

    public void deleteCheckBox(Long card_id, Long checkbox_id, User user) {
        // 카드 id 확인
        Card card = cardAdapter.findById(card_id);

        // user id 확인
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), card.getColumn().getBoard().getId())){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 체크박스 id 확인
        CheckBox checkBox = checkBoxAdapter.findById(checkbox_id);

        // 체크박스 삭제
        checkBoxAdapter.delete(checkBox);
    }

    @Transactional
    public void checkToggle(Long card_id, Long checkbox_id, User user) {
        // 카드 id 확인
        Card card = cardAdapter.findById(card_id);

        // user id 확인
        if(!userAndBoardAdapter.existsByUserIdAndBoardId(user.getId(), card.getColumn().getBoard().getId())){
            throw new UserNotBoardMemberException(ResponseExceptionEnum.USER_NOT_BOARD_MEMBER);
        }

        // 체크박스 id 확인
        CheckBox checkBox = checkBoxAdapter.findById(checkbox_id);

        // 체크박스 boolean 값 변환
        CheckBox checkedBox = checkBox.checkToggle(checkBox);
    }
}
