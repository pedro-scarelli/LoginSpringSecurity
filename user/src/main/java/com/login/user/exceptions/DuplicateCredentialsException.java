package com.login.user.exceptions;

public class DuplicateCredentialsException extends RuntimeException {
    
    public DuplicateCredentialsException(String message){
        super(message);
    }
}
