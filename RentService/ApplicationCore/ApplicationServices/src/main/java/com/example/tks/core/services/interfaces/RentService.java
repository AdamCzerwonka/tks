package com.example.tks.core.services.interfaces;

import com.example.tks.core.domain.exceptions.*;
import com.example.tks.core.domain.model.Rent;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentService {
    List<Rent> get();

    Rent getById(UUID id) throws NotFoundException;

    List<Rent> getByRealEstateID(UUID realEstateId, boolean current);

    List<Rent> getByClientId(UUID clientId, boolean current);

    Rent create(UUID clientId, UUID realEstateId, LocalDate startDate) throws NotFoundException, AccountInactiveException, RealEstateRentedException;

    void delete(UUID id) throws RentEndedException;

    void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException;

}
