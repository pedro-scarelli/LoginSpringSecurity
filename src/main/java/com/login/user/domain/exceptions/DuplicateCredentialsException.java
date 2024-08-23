package com.login.user.domain.exceptions;

public class DuplicateCredentialsException extends RuntimeException {
    
    public DuplicateCredentialsException(){
        super("E-mail ou login duplicados.");
    }
}
