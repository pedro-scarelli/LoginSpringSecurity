package com.login.user.controllers;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.login.user.domain.dtos.request.*;
import com.login.user.domain.dtos.response.*;
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
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }

        var newUser = userService.createUser(createUserRequestDto);
        var location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newUser.getId())
                    .toUri();
        var message = Map.of("message", "Usuário criado com sucesso", "userId", newUser.getId());

        return ResponseEntity.created(location).body(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(description = "Busca todos os usuários")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna todos os usuários"),
        @ApiResponse(responseCode = "403", description = "Não autorizado")
    })
    @GetMapping
    public ResponseEntity<UserPaginationResponseDTO> getAllUsers(@Valid GetAllUserRequestDTO getAllUserRequestDto) {
        return ResponseEntity.ok(userService.getAllUsers(getAllUserRequestDto.page(), getAllUserRequestDto.items()));
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
        var userResponseDto = new UserResponseDTO(userFound.getId(), userFound.getName(),userFound.getEmail(), userFound.isEnabled());

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
        var userDto = new UserResponseDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail(), updatedUser.isEnabled());

        return ResponseEntity.ok().body(userDto);
    }

    @Operation(description = "Deleta um usuário pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable("id") UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var deletedUser = userService.deleteUser(id);
        Map<String, Object> message = Map.of("message", "Usuário deletado com sucesso", "userId", deletedUser.getId());

        return ResponseEntity.ok().body(message);
    }

    @Operation(description = "Ativa um usuário pelo link mandado pelo e-mail")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/activate/{id}")
    public ResponseEntity<Void> activateUser(@PathVariable("id") UUID id) {
        userService.activateUser(id);

        return ResponseEntity.noContent().build();
    }
}
