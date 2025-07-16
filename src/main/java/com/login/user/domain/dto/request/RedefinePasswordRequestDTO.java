package com.login.user.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RedefinePasswordRequestDTO(@NotBlank String otpCode, @NotBlank String newPassword, @Email String email) { }
