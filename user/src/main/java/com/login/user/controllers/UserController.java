package com.login.user.controllers;

import com.login.user.domain.dtos.AuthenticationDto;
import com.login.user.domain.dtos.ListUserDto;
import com.login.user.domain.dtos.LoginResponseDto;
import com.login.user.domain.dtos.UserDto;
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
    public ResponseEntity<ListUserDto> getAllUsers(@RequestParam int page, @RequestParam int items) {
        ListUserDto usersList = new ListUserDto(userService.getAllUsers(page, items));
        return ResponseEntity.ok(usersList);
    }

    @Operation(description = "Busca um usuário pelo login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID id) {
        User userFound = userService.getUserById(id);
        UserDto userDto = new UserDto(userFound.getName(),userFound.getMail(), userFound.getLogin(), userFound.getPassword());

        return ResponseEntity.ok(userDto);
    }

    @Operation(description = "Faz o registro de um usuário no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna o usuário criado"),
        @ApiResponse(responseCode = "400", description = "Retorna os erros do formulário caso tenha algum campo inválido, ou retorna junto a mensagem \"E-mail ou login duplicado\"")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDto registerUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrors(result));
        }

        userService.registerUser(registerUserDto);

        return ResponseEntity.ok().body(registerUserDto);
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
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserDto updateUserDto, BindingResult result) {
        userService.updateUser(id, updateUserDto);
        return ResponseEntity.ok().body(updateUserDto);
    }

    @Operation(description = "Deleta um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") UUID id) {
        User deletedUser = userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuário " + deletedUser.getName() + " deletado com sucesso!");
    }

    @Operation(description = "Deleta todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos os usuários foram deletados com sucesso"),
        @ApiResponse(responseCode = "403", description = "Token inválido")
    })
    @DeleteMapping("/delete_all")
    public ResponseEntity<String> deleteAllUsers() {
        userService.deleteAllUsers();
        
        return ResponseEntity.ok().body("Todos os usuários foram deletados com sucesso!");
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