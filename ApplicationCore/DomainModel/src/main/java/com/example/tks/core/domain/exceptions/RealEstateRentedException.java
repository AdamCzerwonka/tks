package com.example.tks.core.domain.exceptions;

import java.util.UUID;

public class RealEstateRentedException extends Exception {
    public RealEstateRentedException(UUID id) {
        super("Real estate with given id " + id + " has opened rents");
    }
}
