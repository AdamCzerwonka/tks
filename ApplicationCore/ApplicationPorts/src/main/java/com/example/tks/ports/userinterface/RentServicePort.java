package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentServicePort<T, C> {
    List<T> get();

    T getById(UUID id) throws NotFoundException;

    List<T> getByClientId(UUID clientId, boolean current);

    List<T> getByRealEstateId(UUID realEstateId, boolean current);

    T create(C rentCreateRequest) throws NotFoundException, AccountInactiveException, RealEstateRentedException;

    void delete(UUID id) throws RentEndedException;

    void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException;
}
