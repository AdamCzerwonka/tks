package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RealEstateServicePort<T, R> {
    List<T> get();

    T getById(UUID id) throws NotFoundException;

    T create(R realEstateRequest);

    T update(R realEstateRequest);

    void delete(UUID id) throws RealEstateRentedException;
}
