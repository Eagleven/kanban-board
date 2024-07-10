package com.sparta.kanbanboard.domain.user.controller;

import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> signup(
            @RequestBody SignupRequestDto requestDto
    ){
        String username = userService.signup(requestDto);
        return ResponseEntity.status(ResponseCodeEnum.USER_SUCCESS_SIGNUP.getHttpStatus()).body( username + ResponseCodeEnum.USER_SUCCESS_SIGNUP.getMessage());
    }

}
