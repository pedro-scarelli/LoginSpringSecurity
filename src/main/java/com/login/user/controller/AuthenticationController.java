package com.login.user.controller;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.login.user.domain.dto.request.auth.LoginRequestDTO;
import com.login.user.domain.dto.request.auth.RedefinePasswordRequestDTO;
import com.login.user.domain.dto.request.user.EmailRequestDTO;
import com.login.user.domain.dto.response.SuccessResponseDTO;
import com.login.user.service.*;

import jakarta.validation.Valid;

@AllArgsConstructor
@RequestMapping(path = "/v1/auth", produces = "application/json")
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    private TokenService tokenService;


    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<@NonNull SuccessResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDto) {
        var authenticatedUser = authenticationService.authenticateLogin(loginRequestDto);
        var token = tokenService.generateToken(authenticatedUser);

        var response = new SuccessResponseDTO("Login efetuado com sucesso", Map.of("jwtAuthenticationToken", token));

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/redefine-password/activate", consumes = "application/json")
    public ResponseEntity<@NonNull SuccessResponseDTO> activateUserRedefinePassword(@RequestBody @Valid EmailRequestDTO emailRequestDto) {
        authenticationService.activateRedefinePassword(emailRequestDto.email());
        var response = new SuccessResponseDTO("Código para redefinição de senha enviado");

        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "/redefine-password", consumes = "application/json")
    public ResponseEntity<@NonNull SuccessResponseDTO> redefinePassword(@RequestBody @Valid RedefinePasswordRequestDTO redefinePasswordRequestDto) {
        authenticationService.redefinePassword(
                redefinePasswordRequestDto.otpCode(),
                redefinePasswordRequestDto.newPassword(),
                redefinePasswordRequestDto.email()
            );
        var response = new SuccessResponseDTO("Senha redefinida com sucesso");

        return ResponseEntity.ok(response);
    }
}
