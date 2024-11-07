package com.login.user.controllers;

import com.login.user.domain.dtos.AuthenticationDto;
import com.login.user.domain.dtos.LoginResponseDto;
import com.login.user.domain.dtos.UserDto;
import com.login.user.domain.dtos.RegisterUserDto;
import com.login.user.domain.models.User;
import com.login.user.services.AuthenticateUserService;
import com.login.user.services.TokenService;
import com.login.user.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/v1/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticateUserService authenticateUserService;

    @Autowired
    private TokenService tokenService;

    @Operation(description = "Busca todos os usuários no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna todos os usuários"),
        @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    @GetMapping
    public ResponseEntity<Map<String, List<UserDto>>> getAllUsers(@RequestParam int page, @RequestParam int items) {
        Map<String, List<UserDto>> users = Map.of("users", userService.getAllUsers(page, items));
        return ResponseEntity.ok(users);
    }

    @Operation(description = "Busca um usuário pelo login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        User userFound = userService.getUserById(id);
        UserDto userDto = new UserDto(userFound.getId(), userFound.getName(),userFound.getMail(), userFound.getLogin());

        return ResponseEntity.ok(userDto);
    }

    @Operation(description = "Faz o registro de um usuário no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna o usuário criado."),
        @ApiResponse(responseCode = "400", description = "Retorna os erros do formulário caso tenha algum campo inválido, ou retorna junto a mensagem \"E-mail ou login duplicado\"")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrors(result));
        }

        userService.registerUser(registerUserDto);

        Map<String, String> message = Map.of("message", "Usuário " + registerUserDto.login() + " criado com sucesso!");
        return ResponseEntity.created(null).body(message);
    }

    @Operation(description = "Faz o login do usuário com login e autenticação da senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o token que vai ser utilizado pelo usuário automaticamente"),
        @ApiResponse(responseCode = "400", description = "Retorna login incorreto ou senha incorreta")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationDto loginData) {
        User authenticatedUser = authenticateUserService.authenticateLogin(loginData);
        var token = tokenService.generateToken(authenticatedUser);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
    
    @Operation(description = "Atualiza um usuário do repositório pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody RegisterUserDto updateUserDto, BindingResult result) {
        User updatedUser = userService.updateUser(id, updateUserDto);
        UserDto userDto = new UserDto(updatedUser.getId(), updatedUser.getName(), updatedUser.getMail(), updatedUser.getLogin());
        return ResponseEntity.ok().body(userDto);
    }

    @Operation(description = "Deleta um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") UUID id) {
        User deletedUser = userService.deleteUser(id);
        Map<String, String> message = Map.of("message", "Usuário " + deletedUser.getName() + " deletado com sucesso!");

        return ResponseEntity.ok().body(message);
    }

    @Operation(description = "Deleta todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos os usuários foram deletados com sucesso"),
        @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    @DeleteMapping("/delete_all")
    public ResponseEntity<Map<String, String>> deleteAllUsers() {
        userService.deleteAllUsers();
        
        Map<String, String> message = Map.of("message", "Todos os usuários foram deletados com sucesso!");
        return ResponseEntity.ok().body(message);
    }

    private String validationErrors(BindingResult result) {
        StringBuilder errors = new StringBuilder();
        result.getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errors.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append("\n");
            } else {
                errors.append(error.getDefaultMessage()).append("\n");
            }
        });
        return errors.toString();
    }
}