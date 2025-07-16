package com.login.user.domain.model.enums;

import java.util.List;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum UserRole {
    ADMIN(List.of("ROLE_ADMIN", "ROLE_USER")),
    USER(List.of("ROLE_USER"));

    private final List<SimpleGrantedAuthority> authorities;

    UserRole(List<String> authorities) {
        this.authorities = authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

}

