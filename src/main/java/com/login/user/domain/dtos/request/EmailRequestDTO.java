package com.login.user.domain.dtos.request;

import jakarta.validation.constraints.Email;

public record EmailRequestDTO(@Email String email) { }
