package com.example.pasik.managers.impl;

import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.exceptions.RealEstateRentedException;
import com.example.pasik.managers.RealEstateManager;
import com.example.pasik.model.RealEstate;
import com.example.pasik.model.Rent;
import com.example.pasik.repositories.RealEstateRepository;
import com.example.pasik.repositories.RentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RealEstateManagerImpl implements RealEstateManager {
    private final RealEstateRepository realEstateRepository;
    private final RentRepository rentRepository;

    public RealEstateManagerImpl(final RealEstateRepository realEstateRepository, final RentRepository rentRepository) {
        this.realEstateRepository = realEstateRepository;
        this.rentRepository = rentRepository;
    }

    public RealEstate create(RealEstate realEstate) {
        return realEstateRepository.create(realEstate);
    }

    @Override
    public List<RealEstate> get() {
        return realEstateRepository.get();
    }

    @Override
    public RealEstate getById(UUID id) throws NotFoundException {
        Optional<RealEstate> realEstate = realEstateRepository.getById(id);
        if (realEstate.isEmpty()) {
            throw new NotFoundException("Real estate with given id does not exists");
        }

        return realEstate.get();
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
