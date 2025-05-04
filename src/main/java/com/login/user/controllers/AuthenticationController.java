package com.login.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.user.domain.dtos.request.LoginRequestDTO;
import com.login.user.domain.dtos.response.LoginResponseDTO;
import com.login.user.domain.models.User;
import com.login.user.services.AuthenticateUserService;
import com.login.user.services.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/v1/auth")
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticateUserService authenticateUserService;

    @Autowired
    private TokenService tokenService;


    @Operation(description = "Faz o login do usuário com login e senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o token Bearer do usuário"),
        @ApiResponse(responseCode = "400", description = "Retorna login incorreto ou senha incorreta")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDto) {
        User authenticatedUser = authenticateUserService.authenticateLogin(loginRequestDto);
        var token = tokenService.generateToken(authenticatedUser);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
