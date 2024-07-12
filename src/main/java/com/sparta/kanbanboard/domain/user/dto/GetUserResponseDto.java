package com.sparta.kanbanboard.domain.user.dto;

import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.utils.Role;
import lombok.Getter;

@Getter
public class GetUserResponseDto {

    private Long id;
    private String username;
    private Role role;
    private String name;
    private String email;


    public GetUserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getUserRole();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
