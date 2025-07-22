package com.login.user.domain.dto.request.address;

import jakarta.validation.constraints.*;

public record CreateAddressRequestDTO(

        @NotBlank(message = "Rua não pode estar em branco.")
        @Size(min = 3, max = 255, message = "Rua deve ter entre 3 e 255 caracteres.")
        String street,

        @Min(value = 1, message = "Número deve ser maior ou igual a 1.")
        @Max(value = 1000000, message = "Número deve ser menor ou igual a 1.000.000.")
        int number,

        @Size(max = 100, message = "Complemento pode ter no máximo 100 caracteres.")
        String complement,

        @NotBlank(message = "Bairro é obrigatório.")
        @Size(max = 100, message = "Bairro pode ter no máximo 100 caracteres.")
        String neighborhood,

        @NotBlank(message = "Cidade é obrigatória.")
        @Size(max = 100, message = "Cidade pode ter no máximo 100 caracteres.")
        String city,

        @NotBlank(message = "Estado é obrigatório.")
        @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve conter exatamente 2 letras maiúsculas (ex: SP).")
        String state,

        @NotBlank(message = "CEP é obrigatório.")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP deve estar no formato 00000-000.")
        String zipCode,

        @NotBlank(message = "País é obrigatório.")
        @Size(max = 100, message = "País pode ter no máximo 100 caracteres.")
        String country
) {}