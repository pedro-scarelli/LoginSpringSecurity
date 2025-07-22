package com.login.user.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.login.user.domain.dto.request.client.CreateClientRequestDTO;
import com.login.user.domain.dto.response.SuccessResponseDTO;
import com.login.user.service.ClientService;
import com.login.user.util.ValidationUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@RequestMapping(path = "/v1/client", produces = "application/json")
@RestController
public class ClientController {
    
    private final ClientService clientService;
    
    @PostMapping(consumes = "application/json")
    public ResponseEntity<@NonNull Object> createClient(@Valid @RequestBody CreateClientRequestDTO createClientRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }

        var newClient = clientService.createClient(createClientRequestDto);
        var successResponse = new SuccessResponseDTO("Cliente cadastrado com sucesso", Map.of("clientId", newClient.getId()));

        return ResponseEntity.created(null).body(successResponse);
    }
    
}
