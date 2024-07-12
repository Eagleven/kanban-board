package com.sparta.kanbanboard.domain.user.service;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.dto.GetUserResponseDto;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import com.sparta.kanbanboard.domain.user.repository.UserRepository;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import com.sparta.kanbanboard.exception.user.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter adapter;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String signup(SignupRequestDto requestDto) {
        log.info("/users, 회원가입 실행 중~");

        try {
            log.info("{} 정보 잘 들어가는 중~", requestDto.getUsername());
            adapter.findDuplicatedUser(requestDto);

            User userEntity = User.builder()
                    .username(requestDto.getUsername())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .name(requestDto.getName())
                    .email(requestDto.getEmail())
                    .build();

            userRepository.save(userEntity);
            return userEntity.getUsername();
        } catch (UserDuplicatedException e) {
            throw new UserException(ResponseExceptionEnum.USER_ALREADY_EXIST);
        } catch (RuntimeException e) {
            throw new UserException(ResponseExceptionEnum.USER_FAIL_SIGNUP);
        }

    }

    @Transactional
    public void subscription(UserDetailsImpl user) {
        adapter.findById(user.getUser().getId());
    }

    @Transactional(readOnly = true)
    public Page<GetUserResponseDto> getUsersWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userRepository.findAll(pageable)
                .map(GetUserResponseDto::new);
    }

    public GetUserResponseDto getUser(Long userId, UserDetailsImpl userDetails) {
        return adapter.getUser(userId);
    }
}
