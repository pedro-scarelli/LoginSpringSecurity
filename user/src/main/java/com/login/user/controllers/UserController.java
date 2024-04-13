package com.login.user.controllers;

import com.login.user.dtos.AuthenticationDto;
import com.login.user.dtos.LoginResponseDto;
import com.login.user.dtos.UserDto;
import com.login.user.models.User;
import com.login.user.services.AuthUserService;
import com.login.user.services.TokenService;
import com.login.user.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUserService authUser;

    @Autowired
    private TokenService tokenService;

    @Operation(description = "Busca todos os usuários no repositório")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna todos os usuários"),
        @ApiResponse(responseCode = "400", description = "Não existe nenhum usuário salvo")
    })
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(description = "Busca um usuário pelo login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @GetMapping("/users/{login}")
    public ResponseEntity<UserDto> getUser(@PathVariable String login) {
        UserDto userDto = userService.getUserByLogin(login);
        return ResponseEntity.ok(userDto);
    }

    @Operation(description = "Faz o registro de um usuário no banco de dados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna o usuário criado"),
        @ApiResponse(responseCode = "400", description = "Retorna os erros do formulário caso tenha algum campo inválido, ou retorna junto a mensagem \"E-mail ou login duplicado\"")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(validationErrors(result));
        }

        userService.registerUser(userDto);

        return ResponseEntity.ok().body(userDto);
    }

    @Operation(description = "Faz o login do usuário com login e autenticação da senha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o token que vai ser utilizado pelo usuário automaticamente"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDto data) {
        UserDto userDto = authUser.authenticateLogin(data);
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }
    
    @Operation(description = "Atualiza um usuário do repositório pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @PutMapping("/editUser/{id}")
    public ResponseEntity<UserDto> editUser(@PathVariable("id") UUID id, @Valid @RequestBody UserDto userDto, BindingResult result) {
        userService.updateUser(id, userDto);
        return ResponseEntity.ok().body(userDto);
    }

    @Operation(description = "Deleta um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Não existe nenhum usuário salvo com esse login")
    })
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") UUID id) {
        UserDto userDto = userService.deleteUser(id);
        return ResponseEntity.ok().body("Usuário " + userDto.name() + " deletado com sucesso!");
    }

    @Operation(description = "Deleta todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todos os usuários foram deletados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Não foi possível excluir todos os usuários")
    })
    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<Object> deleteAllUsers() {
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