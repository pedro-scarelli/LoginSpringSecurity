package com.login.user.domain.dto.request;

import jakarta.validation.constraints.Email;

public record EmailRequestDTO(@Email String email) { }
