package com.login.user.domain.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(min = 6, max = 100) String password
    ) {}
