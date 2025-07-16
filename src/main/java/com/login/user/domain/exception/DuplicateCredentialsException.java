package com.login.user.domain.exception;

public class DuplicateCredentialsException extends RuntimeException {

    public DuplicateCredentialsException() {
        super("E-mail ou login duplicados.");
    }
}
