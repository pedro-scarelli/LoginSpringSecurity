package com.login.user.controller;

import java.util.Map;
import java.util.UUID;

import com.login.user.domain.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.login.user.domain.dto.request.*;
import com.login.user.domain.dto.response.*;
import com.login.user.service.UserService;
import com.login.user.util.ValidationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@AllArgsConstructor
@Tag(name = "User Management", description = "Endpoints para CRUD e ativação de usuários")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@RequestMapping(path = "/v1/user", produces = "application/json")
@RestController
public class UserController {

    private UserService userService;

    private UserMapper userMapper;

    @Operation(
        summary = "Cadastrar novo usuário",
        description = "Cria um novo usuário no sistema e envia e-mail de confirmação"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuário criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"message\": \"Usuário criado com sucesso\", \"userId\": \"<uuid>\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Campos inválidos ou e-mail duplicado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"fieldErrors\": { \"email\": \"Email inválido\" } }"
                )
            )
        )
    })
    @PostMapping(consumes = "application/json")
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
    @Operation(
        summary = "Listar usuários paginados",
        description = "Retorna lista paginada de usuários, podendo filtrar por página e quantidade",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de usuários retornada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserPaginationResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping()
    public ResponseEntity<UserPaginationResponseDTO> getAllUsers(@Valid GetAllUserRequestDTO getAllUserRequestDto) {
        return ResponseEntity.ok(userService.getAllUsers(getAllUserRequestDto.page(), getAllUserRequestDto.items()));
    }

    @Operation(
        summary = "Obter usuário por ID",
        description = "Retorna os dados de um usuário pelo seu ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var userFound = userService.getUserById(id);

        return ResponseEntity.ok(userMapper.toDto(userFound));
    }

    @Operation(
        summary = "Atualizar usuário",
        description = "Atualiza dados de um usuário existente pelo ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserResponseDTO.class)
            )
        ),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Object> updateUser(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDto,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }
        ValidationUtils.isTargetUserSameFromRequest(id);
        var updatedUser = userService.updateUser(id, updateUserRequestDto);

        return ResponseEntity.ok().body(userMapper.toDto(updatedUser));
    }

    @Operation(
        summary = "Excluir usuário",
        description = "Marca um usuário como excluído pelo seu ID",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Usuário deletado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    example = "{ \"message\": \"Usuário deletado com sucesso\", \"userId\": \"<uuid>\" }"
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable("id") UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var deletedUser = userService.deleteUser(id);
        Map<String, Object> message = Map.of("message", "Usuário deletado com sucesso", "userId", deletedUser.getId());

        return ResponseEntity.ok().body(message);
    }

    @Operation(
        summary = "Ativar usuário",
        description = "Ativa um usuário inativo via link de e-mail"
    )
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
