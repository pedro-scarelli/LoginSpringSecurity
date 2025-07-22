package com.login.user.domain.exception;

public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException() {
        super("Empresa não encontrada!");
    }
}
