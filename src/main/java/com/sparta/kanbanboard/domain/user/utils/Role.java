package com.sparta.kanbanboard.domain.user.utils;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    MANAGER("MANAGER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }
}