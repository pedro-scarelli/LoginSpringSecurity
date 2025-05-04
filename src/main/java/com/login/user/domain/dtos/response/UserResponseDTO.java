package com.login.user.domain.dtos.response;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDTO(UUID id, @NotBlank String name, @NotBlank String mail, @NotBlank String login) {}
