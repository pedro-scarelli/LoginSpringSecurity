package com.login.user.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.user.domain.dtos.request.LoginRequestDTO;
import com.login.user.domain.dtos.request.UUIDRequestDTO;
import com.login.user.services.AuthenticationService;
import com.login.user.services.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/v1/auth")
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;


    @Operation(description = "Faz o login do usuário com e-mail e senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o token Bearer do usuário"),
        @ApiResponse(responseCode = "400", description = "Retorna login incorreto ou senha incorreta")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequestDTO loginRequestDto) {
        var authenticatedUser = authenticationService.authenticateLogin(loginRequestDto);
        var token = tokenService.generateToken(authenticatedUser);

        var response = Map.of("message", "Login efetuado com sucesso", "jwtAuthenticationToken", token);

        return ResponseEntity.ok(response);
    }

    @Operation(description = "Ativa a redefinição de senha para um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Código OTP enviado para o e-mail"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PostMapping("/redefine-password")
    public ResponseEntity<Map<String, String>> activateUserRedefinePassword(@RequestBody @Valid UUIDRequestDTO uuidRequestDto) {
        authenticationService.redefinePassword(uuidRequestDto.id());
        var response = Map.of("message", "Código para redefinição de senha enviado");

        return ResponseEntity.ok(response);
    }
}
