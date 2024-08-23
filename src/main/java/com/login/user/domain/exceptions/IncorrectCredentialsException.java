package com.login.user.domain.exceptions;

public class IncorrectCredentialsException extends RuntimeException {
    
    public IncorrectCredentialsException(String message){
        super(message);
    }
}
