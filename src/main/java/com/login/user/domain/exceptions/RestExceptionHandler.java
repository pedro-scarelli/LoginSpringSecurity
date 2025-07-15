package com.login.user.domain.exceptions;

import java.util.Map;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.login.user.domain.dtos.response.ErrorResponseDTO;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<Map<String, String>> userNotFoundHandler(UserNotFoundException exception){
        var errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(DuplicateCredentialsException.class)
    private ResponseEntity<Map<String, String>> duplicateCredentialsHandler(DuplicateCredentialsException exception){
        var errors = Map.of("message", exception.getMessage());
 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    private ResponseEntity<Map<String, String>> incorrectCredentialsHandler(IncorrectCredentialsException exception){
        var errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UnauthorizedException.class)
    private ResponseEntity<Map<String, String>> unauthorizedExceptionHandler(UnauthorizedException exception){
        var errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(UserNotActivatedException.class)
    private ResponseEntity<Map<String, String>> userNotActivatedExceptionHandler(UserNotActivatedException exception){
        var errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @ExceptionHandler(EmailMessagingException.class)
    private ResponseEntity<Map<String, String>> emailMessagingExceptionHandler(EmailMessagingException exception){
        var errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
 
        var errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList();

        var errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST,
            request.getDescription(false).replace("uri=", ""),
            errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
