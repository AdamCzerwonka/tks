package com.example.tks.adapter.data.aggregates;

import com.example.tks.adapter.data.model.RealEstateEnt;
import com.example.tks.adapter.data.repositories.RealEstateRepository;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.ports.infrastructure.RealEstatePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RealEstateRepositoryAdapter implements RealEstatePort {
    private final RealEstateRepository realEstateRepository;

    @Override
    public List<RealEstate> get() {
        List<RealEstateEnt> result = realEstateRepository.get();
        return result.stream().map(RealEstateEnt::toRealEstate).toList();
    }

    @Override
    public Optional<RealEstate> getById(UUID id) {
        return realEstateRepository.getById(id).map(RealEstateEnt::toRealEstate);
    }

    @Override
    public RealEstate create(RealEstate realEstate) {
        return realEstateRepository.create(RealEstateEnt.toRealEstateEnt(realEstate)).toRealEstate();
    }

    @Override
    public RealEstate update(RealEstate realEstate) {
        return realEstateRepository.update(RealEstateEnt.toRealEstateEnt(realEstate)).toRealEstate();
    }

    @Override
    public void delete(UUID id) {
        realEstateRepository.delete(id);
    }
}
