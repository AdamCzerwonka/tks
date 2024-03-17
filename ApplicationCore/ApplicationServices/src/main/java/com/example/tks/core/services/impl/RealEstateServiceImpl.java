package com.example.tks.core.services.impl;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.ports.infrastructure.RealEstatePort;
import com.example.tks.ports.infrastructure.RentPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RealEstateServiceImpl implements RealEstateService {
    private final RealEstatePort realEstateRepository;
    private final RentPort rentRepository;

    @Override
    public RealEstate getById(UUID id) throws NotFoundException {
        Optional<RealEstate> realEstate = realEstateRepository.getById(id);
        if (realEstate.isEmpty()) {
            throw new NotFoundException("Real estate with given id does not exists");
        }

        return realEstate.get();
    }

    @Override
    public List<RealEstate> get() {
        return realEstateRepository.get();
    }

    public RealEstate create(RealEstate realEstate) {
        return realEstateRepository.create(realEstate);
    }

    @Override
    public RealEstate update(RealEstate realEstate) {
        return realEstateRepository.update(realEstate);
    }

    @Override
    public void delete(UUID id) throws RealEstateRentedException {
        List<Rent> rents = rentRepository.getByRealEstateId(id, true);
        if (!rents.isEmpty()) {
            throw new RealEstateRentedException(id);
        }

        realEstateRepository.delete(id);
    }
}
