package com.login.user.domain.exception;

import com.login.user.domain.model.RestErrorMessage;
import com.login.user.domain.dto.response.ErrorResponseDTO;

import lombok.NonNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.annotation.PostConstruct;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Map<Class<? extends RuntimeException>, HttpStatus> exceptionStatusMap = new HashMap<>();

    @PostConstruct
    private void init() {
        exceptionStatusMap.put(UserNotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionStatusMap.put(DuplicateCredentialsException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(IncorrectCredentialsException.class, HttpStatus.BAD_REQUEST);
        exceptionStatusMap.put(UnauthorizedException.class, HttpStatus.UNAUTHORIZED);
        exceptionStatusMap.put(UserNotActivatedException.class, HttpStatus.UNAUTHORIZED);
        exceptionStatusMap.put(EmailMessagingException.class, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<@NonNull RestErrorMessage> handleKnownExceptions(RuntimeException ex) {
        HttpStatus status = exceptionStatusMap.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return buildResponse(ex, status);
    }

    private ResponseEntity<@NonNull RestErrorMessage> buildResponse(RuntimeException ex, HttpStatus status) {
        var error = new RestErrorMessage(status, ex.getMessage());
        return ResponseEntity.status(status).body(error);
    }

    @Override
    protected ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {

        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        var errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST,
            request.getDescription(false).replace("uri=", ""),
            errors
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
