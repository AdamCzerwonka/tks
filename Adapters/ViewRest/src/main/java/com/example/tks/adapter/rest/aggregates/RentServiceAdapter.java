package com.example.tks.adapter.rest.aggregates;

import com.example.tks.adapter.rest.model.Rent.RentCreateRequest;
import com.example.tks.adapter.rest.model.Rent.RentResponse;
import com.example.tks.core.domain.exceptions.*;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RentService;
import com.example.tks.ports.userinterface.RentServicePort;

import java.util.List;
import java.util.UUID;

public class RentServiceAdapter implements RentServicePort<RentResponse, RentCreateRequest> {
    private final RentService service;

    public RentServiceAdapter(RentService service) {
        this.service = service;
    }

    @Override
    public RentResponse getById(UUID id) throws NotFoundException {
        return RentResponse.fromRent(service.getById(id));
    }

    @Override
    public List<RentResponse> getByRealEstateId(UUID realEstateId, boolean current) {
        return service.getByRealEstateID(realEstateId, current).stream().map(RentResponse::fromRent).toList();
    }

    @Override
    public List<RentResponse> getByClientId(UUID clientId, boolean current) {
        return service.getByClientId(clientId, current).stream().map(RentResponse::fromRent).toList();
    }

    @Override
    public List<RentResponse> get() {
        return service.get().stream().map(RentResponse::fromRent).toList();
    }

    @Override
    public RentResponse create(RentCreateRequest rentCreateRequest) throws NotFoundException, AccountInactiveException, RealEstateRentedException {
        Rent createdRent = service.create(rentCreateRequest.getClientId(), rentCreateRequest.getRealEstateId(), rentCreateRequest.getStartDate());
        return RentResponse.fromRent(createdRent);
    }

    @Override
    public void delete(UUID id) throws RentEndedException {
        service.delete(id);
    }

    @Override
    public void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException {
        service.endRent(id);
    }
}
