package com.login.user.domain.exceptions;

public class EmailMessagingException extends RuntimeException {

    public EmailMessagingException() {
        super("Erro ao enviar e-mail");
    }

    public EmailMessagingException(String message) {
        super(message);
    }
}
