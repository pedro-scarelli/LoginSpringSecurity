package com.login.user.domain.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(
        @Size(max = 100) String name,
        @Size(min = 6, max = 100) String password
    ) {}
