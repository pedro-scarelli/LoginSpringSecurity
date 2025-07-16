package com.login.user.domain.dto.request;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetAllUserRequestDTO(
        @RequestParam(defaultValue = "1")
        @Min(value = 1, message = "A página deve ser maior que 0")
        int page,

        @RequestParam(defaultValue = "10")
        @Min(value = 1, message = "Deve haver pelo menos 1 item por página")
        @Max(value = 100, message = "Máximo de 100 itens por página")
        int items
        ) {}
