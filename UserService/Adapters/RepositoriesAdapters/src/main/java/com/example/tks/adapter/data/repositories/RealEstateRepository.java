package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.RealEstateEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RealEstateRepository {
    List<RealEstateEnt> get();

    Optional<RealEstateEnt> getById(UUID id);

    RealEstateEnt create(RealEstateEnt realEstate);

    RealEstateEnt update(RealEstateEnt realEstate);

    void delete(UUID id);
}
