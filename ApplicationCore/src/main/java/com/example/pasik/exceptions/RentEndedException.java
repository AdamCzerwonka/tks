package com.example.pasik.exceptions;

public class RentEndedException extends Exception {
    public RentEndedException() {
        super("Rent has already been ended");
    }
}
