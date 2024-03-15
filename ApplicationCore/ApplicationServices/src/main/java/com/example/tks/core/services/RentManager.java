package com.example.tks.core.services;

import com.example.tks.core.domain.exceptions.*;
import com.example.tks.core.domain.model.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentManager {
    Rent create(UUID clientId, UUID realEstateId, LocalDate startDate) throws NotFoundException, AccountInactiveException, RealEstateRentedException;

    void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException;

    List<Rent> getByClientId(UUID clientId, boolean current);

    List<Rent> get();

    Rent getById(UUID id) throws NotFoundException;

    void delete(UUID id) throws RentEndedException;


    List<Rent> getByRealEstateID(UUID realEstateId, boolean current);
}
