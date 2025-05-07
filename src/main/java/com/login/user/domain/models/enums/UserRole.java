package com.login.user.domain.models.enums;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {
    ADMIN(List.of("ROLE_ADMIN", "ROLE_USER")),
    USER(List.of("ROLE_USER"));

    private final List<SimpleGrantedAuthority> authorities;

    UserRole(List<String> authorities) {
        this.authorities = authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }
}

