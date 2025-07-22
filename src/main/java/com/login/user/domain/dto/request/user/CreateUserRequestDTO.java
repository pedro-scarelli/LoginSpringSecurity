package com.login.user.domain.dto.request.user;

import com.login.user.domain.dto.CreatePersonDataDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateUserRequestDTO(

        @Valid
        @NotNull(message = "Os dados pessoais são obrigatórios")
        CreatePersonDataDTO person,

        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres.")
        String password
        
) {}
