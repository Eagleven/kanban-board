package com.sparta.kanbanboard.domain.user.controller;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_SUBSCRIPTION;
import static com.sparta.kanbanboard.common.ResponseCodeEnum.USER_SUCCESS_SIGNUP;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.FAIL_TO_CHANGE_ROLE;

import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseCodeEnum;
import com.sparta.kanbanboard.common.ResponseUtils;
import com.sparta.kanbanboard.common.security.AuthEnum;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.user.dto.GetUserResponseDto;
import com.sparta.kanbanboard.domain.user.dto.PageableResponse;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.domain.user.service.UserService;
import com.sparta.kanbanboard.domain.user.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestHeader("AccessToken") String accessToken) throws IllegalAccessException {
        // 현재 토큰에서 역할을 가져와서 새로운 역할을 설정
        Role currentRole = Role.valueOf(tokenProvider.getUserInfoFromToken(accessToken.substring(7)).get("auth").toString());
        Role newRole = (currentRole.equals(Role.USER)) ? Role.MANAGER : Role.USER;

        // 역할 변경
        userService.subscription(userDetails.getUser());

        try {
            // 새로운 토큰 발급
            String newAccessToken = tokenProvider.reissueAccessToken(userDetails.getUsername(), newRole);
            String newRefreshToken = tokenProvider.reissueRefreshToken(userDetails.getUsername());

            // 응답 헤더에 새로운 토큰 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set(AuthEnum.ACCESS_TOKEN.getValue(), newAccessToken);
            headers.set(AuthEnum.REFRESH_TOKEN.getValue(), newRefreshToken);

            return ResponseUtils.of(SUCCESS_SUBSCRIPTION, newRole);
        } catch (IllegalAccessException e) {
            return ResponseUtils.of(FAIL_TO_CHANGE_ROLE);
        }
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
