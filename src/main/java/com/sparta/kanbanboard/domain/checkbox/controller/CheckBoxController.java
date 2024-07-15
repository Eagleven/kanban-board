package com.sparta.kanbanboard.domain.checkbox.controller;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.checkbox.dto.CheckBoxRequestDto;
import com.sparta.kanbanboard.domain.checkbox.service.CheckBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card/{card_id}/checkboxes")
public class CheckBoxController {

    private final CheckBoxService checkBoxService;

    @PostMapping()
    public ResponseEntity<HttpResponseDto> createCheckBox(@PathVariable Long card_id,
            @RequestBody CheckBoxRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.CHECKBOX_CREATED_SUCCESS,
                checkBoxService.createCheckBox(card_id, requestDto, userDetails.getUser()));
    }

    @PatchMapping("/{checkbox_id}")
    public ResponseEntity<HttpResponseDto> updateCheckBox(@PathVariable Long card_id,
            @PathVariable Long checkbox_id, @RequestBody CheckBoxRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseUtils.of(ResponseCodeEnum.CHECKBOX_UPDATE_SUCCESS,
                checkBoxService.updateCheckBox(card_id, checkbox_id, requestDto,
                        userDetails.getUser()));
    }

    @DeleteMapping("/{checkbox_id}")
    public ResponseEntity<HttpResponseDto> deleteCheckBox(@PathVariable Long card_id,
            @PathVariable Long checkbox_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkBoxService.deleteCheckBox(card_id, checkbox_id, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.CHECKBOX_DELETE_SUCCESS);
    }

    @GetMapping("/{checkbox_id}")
    public ResponseEntity<HttpResponseDto> checkToggle(@PathVariable Long card_id,
            @PathVariable Long checkbox_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkBoxService.checkToggle(card_id, checkbox_id, userDetails.getUser());
        return ResponseUtils.of(ResponseCodeEnum.CHECKBOX_CHECK_SUCCESS);
    }
}
