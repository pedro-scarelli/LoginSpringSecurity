package com.login.user.controller;

import java.util.Map;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.login.user.domain.dto.request.*;
import com.login.user.service.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@AllArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para autenticação de usuários, login e redefinição de senha.")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@RequestMapping(path = "/v1/auth", produces = "application/json")
@RestController
public class AuthenticationController {

    private AuthenticationService authenticationService;

    private TokenService tokenService;


    @Operation(
        summary = "Login de usuário",
        description = "Realiza o login de um usuário com e-mail e senha, retornando um token JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login bem-sucedido, retorna token Bearer",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"message\": \"Login efetuado com sucesso\", \"jwtAuthenticationToken\": \"<token>\" }"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"timestamp\": \"2025-05-07T12:34:56Z\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Login ou senha incorretos\" }"
                )
            )
        )
    })
    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequestDTO loginRequestDto) {
        var authenticatedUser = authenticationService.authenticateLogin(loginRequestDto);
        var token = tokenService.generateToken(authenticatedUser);

        var response = Map.of("message", "Login efetuado com sucesso", "jwtAuthenticationToken", token);

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Ativar redefinição de senha",
        description = "Envia um código OTP para o e-mail do usuário para iniciar o processo de redefinição de senha."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "OTP enviado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"message\": \"Código para redefinição de senha enviado\" }"
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"timestamp\": \"2025-05-07T12:34:56Z\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado\" }"
                )
            )
        )
    })
    @PostMapping(path = "/redefine-password/activate", consumes = "application/json")
    public ResponseEntity<Map<String, String>> activateUserRedefinePassword(@RequestBody @Valid EmailRequestDTO emailRequestDto) {
        authenticationService.activateRedefinePassword(emailRequestDto.email());
        var response = Map.of("message", "Código para redefinição de senha enviado");

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Redefinir senha",
        description = "Redefine a senha do usuário utilizando o código OTP recebido por e-mail."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Senha redefinida com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"message\": \"Senha redefinida com sucesso\" }"
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "OTP inválido ou expirado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"timestamp\": \"2025-05-07T12:40:00Z\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Redefinição de senha não autorizada\" }"
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"timestamp\": \"2025-05-07T12:34:56Z\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado\" }"
                )
            )
        )
    })
    @PatchMapping(path = "/redefine-password", consumes = "application/json")
    public ResponseEntity<Map<String, String>> redefinePassword(@RequestBody @Valid RedefinePasswordRequestDTO redefinePasswordRequestDto) {
        authenticationService.redefinePassword(
                redefinePasswordRequestDto.otpCode(),
                redefinePasswordRequestDto.newPassword(),
                redefinePasswordRequestDto.email()
            );
        var response = Map.of("message", "Senha redefinida com sucesso");

        return ResponseEntity.ok(response);
    }
}
