package com.login.user.domain.dto.request.user;

import jakarta.validation.constraints.Email;

public record EmailRequestDTO(@Email String email) { }
