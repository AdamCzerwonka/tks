package com.example.pasik.managers;

import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.exceptions.RealEstateRentedException;
import com.example.pasik.model.RealEstate;

import java.util.List;
import java.util.UUID;

public interface RealEstateManager {
    RealEstate create(RealEstate realEstate);
    List<RealEstate> get();
    RealEstate getById(UUID id) throws NotFoundException;
    RealEstate update(RealEstate realEstate);
    void delete(UUID id) throws RealEstateRentedException;
}
