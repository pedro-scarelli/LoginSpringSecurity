package com.login.user.domain.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Usuário não autenticado");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
