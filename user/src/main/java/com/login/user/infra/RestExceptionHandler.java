package com.login.user.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.login.user.exceptions.DuplicateCredentialsException;
import com.login.user.exceptions.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<String> userNotFoundHandler(UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
    }
    
    @ExceptionHandler(DuplicateCredentialsException.class)
    private ResponseEntity<String> duplicateCredentialsHandler(DuplicateCredentialsException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
