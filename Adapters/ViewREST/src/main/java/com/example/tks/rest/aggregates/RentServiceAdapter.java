package com.example.tks.rest.aggregates;

import com.example.tks.core.domain.exceptions.*;
import com.example.tks.ports.userinterface.RentServicePort;
import com.example.tks.rest.model.Rent.RentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class RentServiceAdapter implements RentServicePort<RentResponse> {
    @Override
    public RentResponse create(UUID clientId, UUID realEstateId, LocalDate startDate) throws NotFoundException, AccountInactiveException, RealEstateRentedException {
        return null;
    }

    @Override
    public void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException {

    }

    @Override
    public List<RentResponse> getByClientId(UUID clientId, boolean current) {
        return null;
    }

    @Override
    public List<RentResponse> get() {
        return null;
    }

    @Override
    public RentResponse getById(UUID id) throws NotFoundException {
        return null;
    }

    @Override
    public void delete(UUID id) throws RentEndedException {

    }

    @Override
    public List<RentResponse> getByRealEstateID(UUID realEstateId, boolean current) {
        return null;
    }
}
