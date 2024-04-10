package com.login.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
    @NotBlank @Size(max = 100) String name,
     @NotBlank @Email String mail,
      @NotBlank @Size(max = 50) String login,
       @NotBlank @Size(min = 6, max = 100) String password
       ) {
    
}
