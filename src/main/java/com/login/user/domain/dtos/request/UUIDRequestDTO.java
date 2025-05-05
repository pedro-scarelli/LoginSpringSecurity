package com.login.user.domain.dtos.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record UUIDRequestDTO(@NotNull UUID id) {}
