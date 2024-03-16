package com.example.tks.core.services;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;

import java.util.List;
import java.util.UUID;

public interface RealEstateService {
    RealEstate create(RealEstate realEstate);

    List<RealEstate> get();

    RealEstate getById(UUID id) throws NotFoundException;

    RealEstate update(RealEstate realEstate);

    void delete(UUID id) throws RealEstateRentedException;
}
