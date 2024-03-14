package com.example.pasik.exceptions;

public class AccountInactiveException extends Exception {
    public AccountInactiveException() {
        super("Account with given id is not active");
    }
}
