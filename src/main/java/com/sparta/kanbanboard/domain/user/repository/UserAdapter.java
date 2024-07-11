package com.sparta.kanbanboard.domain.user.repository;

import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.exception.user.UserDuplicatedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("This \"%s\" does not exist.", username)));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
