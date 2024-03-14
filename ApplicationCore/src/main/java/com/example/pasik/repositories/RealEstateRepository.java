package com.example.pasik.repositories;

import com.example.pasik.model.RealEstate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RealEstateRepository {
    List<RealEstate> get();
    Optional<RealEstate> getById(UUID id);
    RealEstate create(RealEstate realEstate);
    RealEstate update(RealEstate realEstate);
    void delete(UUID id);
}
