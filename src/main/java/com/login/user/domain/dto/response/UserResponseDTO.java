package com.login.user.domain.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.login.user.domain.model.enums.UserRole;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        boolean isEnabled,
        UserRole role,
        Instant createdAt,
        Instant deletedAt
    ) { }
