package com.login.user.domain.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(
    String type,
    String title,
    int status,
    String detail,
    String instance,
    List<String> errors
) {
    public ErrorResponseDTO(HttpStatus status, String path, List<String> errors) {
        this(
            "about:blank",
            status.getReasonPhrase(),
            status.value(),
            "Erro de validação. Verifique os detalhes.",
            path,
            errors
        );
    }
}
