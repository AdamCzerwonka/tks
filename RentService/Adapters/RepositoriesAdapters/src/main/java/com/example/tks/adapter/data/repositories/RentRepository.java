package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.RentEnt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository {
    List<RentEnt> get();

    List<RentEnt> getByClientId(UUID clientId, boolean current);

    List<RentEnt> getByRealEstateId(UUID realEstateId, boolean current);

    Optional<RentEnt> getById(UUID id);

    RentEnt create(RentEnt rent);

    RentEnt update(RentEnt rent);

    void delete(UUID id);
}
