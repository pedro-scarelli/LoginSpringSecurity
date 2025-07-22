package com.login.user.domain.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record SuccessResponseDTO (
    String message,
    Map<String, Object> data,
    LocalDateTime timestamp
) {
    public SuccessResponseDTO(String message) {
        this(message, null, LocalDateTime.now());
    }
    
    public SuccessResponseDTO(String message, Map<String, Object> data) {
        this(message, data, LocalDateTime.now());
    }   
}
