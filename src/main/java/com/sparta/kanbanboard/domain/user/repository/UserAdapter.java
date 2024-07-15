package com.sparta.kanbanboard.domain.user.repository;

import static com.sparta.kanbanboard.common.CommonStatusEnum.ACTIVE;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.USER_ALREADY_EXIST;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.USER_NOT_FOUND;
import static com.sparta.kanbanboard.domain.user.utils.Role.MANAGER;
import static com.sparta.kanbanboard.domain.user.utils.Role.USER;

import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.dto.GetUserResponseDto;
import com.sparta.kanbanboard.domain.user.dto.SignupRequestDto;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import com.sparta.kanbanboard.exception.user.UserException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAdapter {

    private final UserRepository userRepository;

    public void findDuplicatedUser(SignupRequestDto requestDto) {
        Optional<User> user = userRepository.findUserByUsername(requestDto.getUsername());
        if (user.isPresent()) {
            throw new UserDuplicatedException(USER_ALREADY_EXIST);
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("This \"%s\" does not exist.", username)));
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(Long userId) {
        User user = userRepository.findById(userId).get();
        User userByStatus = userRepository.findUserByIdAndStatus(userId, user.getStatus())
                .orElseThrow(
                        () -> new UserException(USER_NOT_FOUND)
                );

        if (!Objects.equals(user.getUsername(), userByStatus.getUsername())) {
            throw new UserException(USER_NOT_FOUND);
        }

        user.setUserRole(user.getUserRole().equals(USER) ? MANAGER : USER);
        userRepository.save(user);

        return user;
    }


    public GetUserResponseDto getUser(Long userId, User user) {
        User userByStatus = userRepository.findUserByIdAndStatus(userId, ACTIVE)
                .orElseThrow(
                        () -> new UserException(USER_NOT_FOUND)
                );

        return new GetUserResponseDto(user);
    }

    public Page<GetUserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAllByStatus(ACTIVE, pageable).map(GetUserResponseDto::new);
    }
}
