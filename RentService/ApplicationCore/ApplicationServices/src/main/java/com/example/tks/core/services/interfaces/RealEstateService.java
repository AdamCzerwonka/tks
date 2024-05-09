package com.example.tks.core.services.interfaces;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;

import java.util.List;
import java.util.UUID;

public interface RealEstateService {
    List<RealEstate> get();

    RealEstate getById(UUID id) throws NotFoundException;

    RealEstate create(RealEstate realEstate);

    RealEstate update(RealEstate realEstate);

    void delete(UUID id) throws RealEstateRentedException;
}
