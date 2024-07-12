package com.sparta.kanbanboard.domain.user.service;

import static com.sparta.kanbanboard.domain.user.utils.Role.MANAGER;
import static com.sparta.kanbanboard.domain.user.utils.Role.USER;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.dto.GetUserResponseDto;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import com.sparta.kanbanboard.domain.user.repository.UserRepository;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import com.sparta.kanbanboard.exception.user.UserException;
import java.util.Objects;
import java.util.function.Function;
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
    private final UserRepository userRepository;
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



    @Transactional
    public Role subscription(User user) {
        User findedUser = adapter.findById(user.getId());

        if(!Objects.equals(user.getUsername(), findedUser.getUsername())){
            throw new UserException(ResponseExceptionEnum.USER_NOT_FOUND);
        }

        return user.setUserRole(user.getUserRole().equals(USER) ? MANAGER : USER);
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
