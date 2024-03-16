package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;

import java.util.List;
import java.util.UUID;

public interface RealEstateServicePort<T, K> {
    T create(RealEstate realEstate);

    List<T> get();

    T getById(UUID id) throws NotFoundException;

    T update(K realEstate);

    void delete(UUID id) throws RealEstateRentedException;
}
