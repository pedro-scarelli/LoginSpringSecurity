package com.login.user.util;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.login.user.domain.exception.UnauthorizedException;
import com.login.user.domain.model.User;
import com.login.user.domain.model.enums.UserRole;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static String validationErrors(BindingResult result) {
        return result.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    } else {
                        return error.getDefaultMessage();
                    }
                })
                .collect(Collectors.joining("\n"));
    }

    public static void isTargetUserSameFromRequest(UUID targetId) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User userFromToken = (User) authentication.getPrincipal();

        if (!userFromToken.getId().equals(targetId) && userFromToken.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Você não tem permissão para acessar este recurso");
        }
    }
}
