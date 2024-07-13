package com.sparta.kanbanboard.domain.user.controller;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_GET_USER;
import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_SUBSCRIPTION;
import static com.sparta.kanbanboard.common.ResponseCodeEnum.USER_SUCCESS_SIGNUP;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.user.dto.GetUserResponseDto;
import com.sparta.kanbanboard.domain.user.dto.PageableResponse;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.domain.user.service.UserService;
import com.sparta.kanbanboard.domain.user.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<HttpResponseDto> signup(
            @RequestBody SignupRequestDto requestDto
    ) {
        String username = userService.signup(requestDto);
        return ResponseUtils.of(USER_SUCCESS_SIGNUP, username);
    }

    @PatchMapping("/subscription")
    public ResponseEntity<HttpResponseDto> subscription(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 역할 변경
        userService.subscription(userDetails);
        return ResponseUtils.of(SUCCESS_SUBSCRIPTION);
    }

    @GetMapping("/user")
    public ResponseEntity<HttpResponseDto> getUser(
            @RequestParam Long userId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        return ResponseUtils.of(SUCCESS_GET_USER, userService.getUser(userId, userDetails));
    }

    @GetMapping
    public ResponseEntity<HttpResponseDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Page<GetUserResponseDto> responseDto = userService.getUsersWithPage(page, size);
        PageableResponse<GetUserResponseDto> responseEntity = new PageableResponse<>(responseDto);
        return ResponseUtils.of(ResponseCodeEnum.SUCCESS_GET_USERS, responseEntity);
    }
}