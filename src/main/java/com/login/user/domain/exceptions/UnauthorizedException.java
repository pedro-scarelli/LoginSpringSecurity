package com.login.user.domain.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Usuário não autenticado");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
