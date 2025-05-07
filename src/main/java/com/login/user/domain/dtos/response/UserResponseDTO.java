package com.login.user.domain.dtos.response;

import java.time.Instant;
import java.util.UUID;

import com.login.user.domain.models.enums.UserRole;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        boolean isEnabled,
        UserRole role,
        Instant createdAt,
        Instant deletedAt
    ) { }
