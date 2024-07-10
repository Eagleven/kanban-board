package com.sparta.kanbanboard.domain.user.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import com.sparta.kanbanboard.exception.user.UserException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    public User save(User user) {
        return userRepository.save(user);
    }
}
