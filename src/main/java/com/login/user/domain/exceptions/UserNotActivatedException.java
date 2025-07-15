package com.login.user.domain.exceptions;

public class UserNotActivatedException extends RuntimeException {

    public UserNotActivatedException() {
        super("Usuário não ativado");
    }
}
