package com.login.user.domain.dtos;


import com.login.user.domain.models.User;

import jakarta.validation.constraints.NotNull;

public record ListUserDto(@NotNull Iterable<User> users) {}
