package com.login.user.domain.exceptions;

public class UserNotActivatedException extends RuntimeException {

    public UserNotActivatedException() {
        super("Usuário não ativado");
    }

    public UserNotActivatedException(String message) {
        super(message);
    }
}
