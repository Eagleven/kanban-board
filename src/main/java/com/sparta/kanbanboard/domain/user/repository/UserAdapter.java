package com.sparta.kanbanboard.domain.user.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.dto.GetUserResponseDto;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import com.sparta.kanbanboard.exception.user.UserException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAdapter {

    private final UserRepository userRepository;

    public void findDuplicatedUser(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if(user.isPresent()){
            throw new UserDuplicatedException(ResponseExceptionEnum.USER_ALREADY_EXIST);
        }
    }

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("This \"%s\" does not exist.", username)));
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                ()-> new UserException(ResponseExceptionEnum.USER_NOT_FOUND)
        );
    }


    public GetUserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(ResponseExceptionEnum.USER_NOT_FOUND)
        );

        return new GetUserResponseDto(user);
    }
}
