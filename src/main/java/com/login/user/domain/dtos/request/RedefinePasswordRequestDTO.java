package com.login.user.domain.dtos.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RedefinePasswordRequestDTO(@NotBlank String otpCode, @NotBlank String newPassword, @NotNull UUID userId) {}
