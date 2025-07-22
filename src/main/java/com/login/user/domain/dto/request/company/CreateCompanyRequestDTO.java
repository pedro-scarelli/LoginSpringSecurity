package com.login.user.domain.dto.request.company;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.UUID;

import com.login.user.domain.dto.request.address.CreateAddressRequestDTO;

public record CreateCompanyRequestDTO(

    @NotBlank(message = "A razão social é obrigatória")
    String legalName,

    String tradeName,

    @NotBlank(message = "O CNPJ é obrigatório")
    @Size(min = 14, max = 14, message = "O CNPJ deve ter entre 14 e 18 caracteres")
    String cnpj,

    String stateRegistration,

    String municipalRegistration,

    @Positive(message = "A hora padrão deve ser maior que zero")
    Double defaultHourlyRate,

    String taxRegime,

    String municipalServiceCode,

    @NotNull(message = "O ID do usuário (dono) é obrigatório")
    UUID ownerId,

    @Valid
    CreateAddressRequestDTO address

) {}
