package com.example.tks.ports.driving;

import java.util.List;
import java.util.UUID;

public interface RealEstateServicePort {
    RealEstate create(RealEstate realEstate);

    List<RealEstate> get();

    RealEstate getById(UUID id) throws NotFoundException;

    RealEstate update(RealEstate realEstate);

    void delete(UUID id) throws RealEstateRentedException;
}