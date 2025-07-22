package com.login.user.domain.dto.request.client;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import com.login.user.domain.dto.CreatePersonDataDTO;

public record CreateClientRequestDTO(

    @Valid
    @NotNull(message = "Os dados pessoais são obrigatórios")
    CreatePersonDataDTO person,

    @NotNull(message = "O ID da empresa é obrigatório")
    UUID companyId

) {}
