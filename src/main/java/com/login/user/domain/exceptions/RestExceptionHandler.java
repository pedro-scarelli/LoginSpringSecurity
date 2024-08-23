package com.login.user.domain.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
}
