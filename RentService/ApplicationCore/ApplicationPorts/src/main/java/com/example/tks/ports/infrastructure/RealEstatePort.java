package com.example.tks.ports.infrastructure;

import com.example.tks.core.domain.model.RealEstate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RealEstatePort {
    List<RealEstate> get();

    Optional<RealEstate> getById(UUID id);

    RealEstate create(RealEstate realEstate);

    RealEstate update(RealEstate realEstate);

    void delete(UUID id);
}
