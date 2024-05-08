package com.example.tks.core.domain.exceptions;

public class InvalidEndRentDateException extends Exception {
    public InvalidEndRentDateException() {
        super("Cannot set end date which is before start date");
    }
}
