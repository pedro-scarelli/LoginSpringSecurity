package com.login.user.domain.dto;

import org.hibernate.validator.constraints.br.CPF;

import com.login.user.domain.dto.request.address.CreateAddressRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.NonNull;

public record CreatePersonDataDTO (

        @NotBlank(message = "Nome é obrigatório.")
        @Size(max = 100, message = "Nome pode ter no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "Email é obrigatório.")
        @Email(message = "Email inválido.")
        String email,

        @NotBlank(message = "CPF é obrigatório.")
        @Size(min = 11, max = 11, message = "CPF deve conter exatamente 11 dígitos sem pontos e vírgula.")
        @CPF(message = "CPF inválido.")
        String cpf,

        @NotBlank(message = "Telefone é obrigatório.")
        @Pattern(regexp = "^\\d{12,14}$", message = "Telefone deve conter entre 12 e 14 dígitos (ex: 554799999999).")
        String phone,

        @NonNull()
        @Valid
        CreateAddressRequestDTO address
        
) { }
