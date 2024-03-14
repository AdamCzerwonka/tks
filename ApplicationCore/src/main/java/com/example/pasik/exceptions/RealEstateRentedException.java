package com.example.pasik.exceptions;

import java.util.UUID;

public class RealEstateRentedException extends Exception {
    public RealEstateRentedException(UUID id) {
        super("Real estate with given id " + id + " has opened rents");
    }
}
