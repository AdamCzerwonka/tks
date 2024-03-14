package com.example.pasik.exceptions;

public class LoginAlreadyTakenException extends Exception {
    public LoginAlreadyTakenException(String login) {
        super("Login " + login + " is already taken");
    }
}
