package com.example.tks.adapter.rest.aggregates;

import com.example.tks.adapter.rest.model.RealEstate.RealEstateRequest;
import com.example.tks.adapter.rest.model.RealEstate.RealEstateResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.ports.userinterface.RealEstateServicePort;

import java.util.List;
import java.util.UUID;

public class RealEstateServiceAdapter implements RealEstateServicePort<RealEstateResponse, RealEstateRequest> {
    private final RealEstateService service;

    public RealEstateServiceAdapter(RealEstateService service) {
        this.service = service;
    }

    @Override
    public RealEstateResponse getById(UUID id) throws NotFoundException {
        return RealEstateResponse.fromRealEstate(service.getById(id));
    }

    @Override
    public List<RealEstateResponse> get() {
        return service.get().stream().map(RealEstateResponse::fromRealEstate).toList();
    }

    @Override
    public RealEstateResponse create(RealEstateRequest realEstateRequest) {
        RealEstate createdRealEstate = service.create(realEstateRequest.toRealEstate());
        return RealEstateResponse.fromRealEstate(createdRealEstate);
    }

    @Override
    public RealEstateResponse update(RealEstateRequest realEstateRequest) {
        RealEstate updatedRealEstate = service.update(realEstateRequest.toRealEstate());
        return RealEstateResponse.fromRealEstate(updatedRealEstate);
    }

    @Override
    public void delete(UUID id) throws RealEstateRentedException {
        service.delete(id);
    }
}
