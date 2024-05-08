package com.example.tks.core.domain.exceptions;

public class RentEndedException extends Exception {
    public RentEndedException() {
        super("Rent has already been ended");
    }
}
