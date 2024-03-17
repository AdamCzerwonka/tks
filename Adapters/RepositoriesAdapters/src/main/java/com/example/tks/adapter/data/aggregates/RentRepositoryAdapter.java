package com.example.tks.adapter.data.aggregates;

import com.example.tks.adapter.data.model.RentEnt;
import com.example.tks.adapter.data.repositories.RentRepository;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.ports.infrastructure.RentPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RentRepositoryAdapter implements RentPort {
    private final RentRepository rentRepository;

    @Override
    public List<Rent> get() {
        List<RentEnt> result = rentRepository.get();
        return result.stream().map(RentEnt::toRent).toList();
    }

    @Override
    public List<Rent> getByClientId(UUID clientId, boolean current) {
        List<RentEnt> result = rentRepository.getByClientId(clientId, current);
        return result.stream().map(RentEnt::toRent).toList();
    }

    @Override
    public List<Rent> getByRealEstateId(UUID realEstateId, boolean current) {
        List<RentEnt> result = rentRepository.getByRealEstateId(realEstateId, current);
        return result.stream().map(RentEnt::toRent).toList();
    }

    @Override
    public Optional<Rent> getById(UUID id) {
        return rentRepository.getById(id).map(RentEnt::toRent);
    }

    @Override
    public Rent create(Rent rent) {
        return rentRepository.create(RentEnt.toRentEnt(rent)).toRent();
    }

    @Override
    public Rent update(Rent rent) {
        return rentRepository.update(RentEnt.toRentEnt(rent)).toRent();
    }

    @Override
    public void delete(UUID id) {
        rentRepository.delete(id);
    }
}
