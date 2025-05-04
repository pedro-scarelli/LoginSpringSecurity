package com.login.user.domain.exceptions;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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
        Map<String, String> errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(DuplicateCredentialsException.class)
    private ResponseEntity<Map<String, String>> duplicateCredentialsHandler(DuplicateCredentialsException exception){
        Map<String, String> errors = Map.of("message", exception.getMessage());
 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IncorrectCredentialsException.class)
    private ResponseEntity<Map<String, String>> incorrectCredentialsHandler(IncorrectCredentialsException exception){
        Map<String, String> errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(UnauthorizedException.class)
    private ResponseEntity<Map<String, String>> unauthorizedExceptionHandler(UnauthorizedException exception){
        Map<String, String> errors = Map.of("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {
 
        var errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getDefaultMessage())
            .toList();

        var errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST,
            request.getDescription(false).replace("uri=", ""),
            errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
