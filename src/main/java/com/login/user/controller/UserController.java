package com.login.user.controller;

import java.util.Map;
import java.util.UUID;

import com.login.user.domain.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.login.user.domain.dto.request.user.*;
import com.login.user.domain.dto.response.user.*;
import com.login.user.domain.dto.response.*;
import com.login.user.service.UserService;
import com.login.user.util.ValidationUtils;

import jakarta.validation.Valid;

@AllArgsConstructor
@RequestMapping(path = "/v1/user", produces = "application/json")
@RestController
public class UserController {

    private UserService userService;

    private UserMapper userMapper;


    @PostMapping(consumes = "application/json")
    public ResponseEntity<@NonNull Object> createUser(@Valid @RequestBody CreateUserRequestDTO createUserRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.validationErrors(result));
        }

        var newUser = userService.createUser(createUserRequestDto);
        var successResponse = new SuccessResponseDTO("Usuário criado com sucesso", Map.of("userId", newUser.getId()));

        return ResponseEntity.created(null).body(successResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<@NonNull UserPaginationResponseDTO> getAllUsers(@Valid GetAllUserRequestDTO getAllUserRequestDto) {
        return ResponseEntity.ok(userService.getAllUsers(getAllUserRequestDto.page(), getAllUserRequestDto.items()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull UserResponseDTO> getUser(@PathVariable UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var userFound = userService.getUserById(id);

        return ResponseEntity.ok(userMapper.toDto(userFound));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<@NonNull Object> updateUser(
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

    @DeleteMapping("/{id}")
    public ResponseEntity<@NonNull SuccessResponseDTO> deleteUser(@PathVariable("id") UUID id) {
        ValidationUtils.isTargetUserSameFromRequest(id);
        var deletedUser = userService.deleteUser(id);
        var message = new SuccessResponseDTO("Usuário deletado com sucesso", Map.of("userId", deletedUser.getId()));

        return ResponseEntity.ok().body(message);
    }

    @GetMapping("/activate/{id}")
    public ResponseEntity<@NonNull Void> activateUser(@PathVariable("id") UUID id) {
        userService.activateUser(id);

        return ResponseEntity.noContent().build();
    }
}
