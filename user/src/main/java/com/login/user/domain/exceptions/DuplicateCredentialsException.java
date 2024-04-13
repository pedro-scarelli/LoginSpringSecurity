package com.login.user.domain.exceptions;

public class DuplicateCredentialsException extends RuntimeException {
    
    public DuplicateCredentialsException(String message){
        super(message);
    }
}
