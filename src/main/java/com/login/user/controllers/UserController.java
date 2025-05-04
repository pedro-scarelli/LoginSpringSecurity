package com.login.user.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.login.user.domain.dtos.request.*;
import com.login.user.domain.dtos.response.*;
import com.login.user.domain.exceptions.UnauthorizedException;
import com.login.user.domain.models.User;
import com.login.user.services.UserService;
import com.login.user.utils.ValidationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/v1/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(description = "Cadastra um usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna o ID do usuário criado."),
        @ApiResponse(responseCode = "400", description = "Retorna os erros do formulário caso tenha algum campo inválido, ou retorna junto a mensagem \"E-mail ou login duplicado\"")
    })
    @PostMapping()
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }

        var newUser = userService.registerUser(registerUserRequestDto);
        var location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newUser.getId())
                    .toUri();
        var message = Map.of("message", "Usuário criado com sucesso", "userId", newUser.getId());

        return ResponseEntity.created(location).body(message);
    }

    @Operation(description = "Busca todos os usuários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna todos os usuários"),
        @ApiResponse(responseCode = "403", description = "Não autorizado")
    })
    @GetMapping
    public ResponseEntity<UserPaginationResponseDTO> getAllUsers(@RequestParam int page, @RequestParam int items) {
        return ResponseEntity.ok(userService.getAllUsers(page, items));
    }

    @Operation(description = "Busca um usuário pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna o usuário procurado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var userFound = userService.getUserById(id);
        var userResponseDto = new UserResponseDTO(userFound.getId(), userFound.getName(),userFound.getEmail());

        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(description = "Atualiza um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDto,
            BindingResult result) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var updatedUser = userService.updateUser(id, updateUserRequestDto);
        var userDto = new UserResponseDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail());

        return ResponseEntity.ok().body(userDto);
    }

    @Operation(description = "Deleta um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("id") UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var deletedUser = userService.deleteUser(id);
        Map<String, String> message = Map.of("message", "Usuário " + deletedUser.getName() + " deletado com sucesso!");

        return ResponseEntity.ok().body(message);
    }
}
