package com.sparta.kanbanboard.common.security.details;

import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.repository.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAdapter adapter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = adapter.findUserByUsername(username);
        return new UserDetailsImpl(user);
    }
}