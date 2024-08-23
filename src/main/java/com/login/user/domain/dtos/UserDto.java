package com.login.user.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record UserDto(
    UUID id,
    @NotBlank String name,
     @NotBlank String mail,
      @NotBlank String login
       ) {}
