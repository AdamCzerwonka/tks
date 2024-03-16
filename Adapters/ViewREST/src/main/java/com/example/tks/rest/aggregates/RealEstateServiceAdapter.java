package com.example.tks.rest.aggregates;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.ports.userinterface.RealEstateServicePort;
import com.example.tks.rest.model.RealEstate.RealEstateResponse;

import java.util.List;
import java.util.UUID;

public class RealEstateServiceAdapter implements RealEstateServicePort<RealEstateResponse> {
    @Override
    public RealEstateResponse create(RealEstate realEstate) {
        return null;
    }

    @Override
    public List<RealEstateResponse> get() {
        return null;
    }

    @Override
    public RealEstateResponse getById(UUID id) throws NotFoundException {
        return null;
    }

    @Override
    public RealEstateResponse update(RealEstate realEstate) {
        return null;
    }

    @Override
    public void delete(UUID id) throws RealEstateRentedException {

    }
}
