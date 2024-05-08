package com.example.tks.core.domain.exceptions;

public class LoginAlreadyTakenException extends Exception {
    public LoginAlreadyTakenException(String login) {
        super("Login " + login + " is already taken");
    }
}
