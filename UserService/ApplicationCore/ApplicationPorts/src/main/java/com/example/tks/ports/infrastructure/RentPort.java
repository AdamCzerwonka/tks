package com.example.tks.ports.infrastructure;

import com.example.tks.core.domain.model.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentPort {
    List<Rent> get();

    List<Rent> getByClientId(UUID clientId, boolean current);

    List<Rent> getByRealEstateId(UUID realEstateId, boolean current);

    Optional<Rent> getById(UUID id);

    Rent create(Rent rent);

    Rent update(Rent rent);

    void delete(UUID id);
}
