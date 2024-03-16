package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RentServicePort<T> {
    T create(UUID clientId, UUID realEstateId, LocalDate startDate) throws NotFoundException, AccountInactiveException, RealEstateRentedException;

    void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException;

    List<T> getByClientId(UUID clientId, boolean current);

    List<T> get();

    T getById(UUID id) throws NotFoundException;

    void delete(UUID id) throws RentEndedException;

    List<T> getByRealEstateID(UUID realEstateId, boolean current);
}
