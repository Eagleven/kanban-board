package com.sparta.kanbanboard.domain.user.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import com.sparta.kanbanboard.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter adapter;
    private final PasswordEncoder passwordEncoder;

    public String signup(SignupRequestDto requestDto) {
        try {
            log.info("/users, 회원가입 실행 중~");

            adapter.findDuplicatedUser(requestDto.getUsername());
            log.info("{} 정보 잘 들어가는 중~", requestDto.getUsername());

            User user = User.builder()
                    .username(requestDto.getUsername())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .name(requestDto.getName())
                    .email(requestDto.getEmail())
                    .build();

            adapter.save(user);
            return user.getName();
        } catch(UserDuplicatedException e){
            throw new UserException(ResponseExceptionEnum.USER_ALREADY_EXIST);
        }
        catch (RuntimeException e) {
            throw new UserException(ResponseExceptionEnum.USER_FAIL_SIGNUP);
        }
    }
}
