package com.example.tks.core.services.impl;

import com.example.tks.core.domain.exceptions.*;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.services.interfaces.RentService;
import com.example.tks.ports.infrastructure.ClientPort;
import com.example.tks.ports.infrastructure.RealEstatePort;
import com.example.tks.ports.infrastructure.RentPort;
import com.example.tks.core.domain.model.Rent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService {
    private final RentPort rentRepository;
    private final ClientPort clientRepository;
    private final RealEstatePort realEstateRepository;

    @Override
    public Rent create(UUID clientId, UUID realEstateId, LocalDate startDate) throws NotFoundException,
            AccountInactiveException, RealEstateRentedException {
        Optional<Client> client = clientRepository.getById(clientId);
        Optional<RealEstate> realEstate = realEstateRepository.getById(realEstateId);

        if (realEstate.isEmpty()) {
            throw new NotFoundException("realEstate with given id does not exist");
        }

        if (client.isEmpty()) {
            throw new NotFoundException("Client with given id does not exist");
        }

        if (!client.get().getActive()) {
            throw new AccountInactiveException();
        }

        var rents = rentRepository.getByRealEstateId(realEstateId, true);
        if (!rents.isEmpty()) {
            throw new RealEstateRentedException(realEstateId);
        }

        Rent rent = Rent
                .builder()
                .client(client.get())
                .realEstate(realEstate.get())
                .startDate(startDate)
                .build();


        return rentRepository.create(rent);
    }

    @Override
    public void endRent(UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException {
        Optional<Rent> rentResult = rentRepository.getById(id);
        if (rentResult.isEmpty()) {
            throw new NotFoundException("Rent with given id does not exist");
        }
        Rent rent = rentResult.get();

        if (rent.getEndDate() != null) {
            throw new RentEndedException();
        }

        rent.setEndDate(LocalDate.now());

        if (rent.getEndDate().isBefore(rent.getStartDate())) {
            throw new InvalidEndRentDateException();
        }

        rentRepository.update(rent);
    }

    @Override
    public List<Rent> getByClientId(UUID clientId, boolean current) {
        return rentRepository.getByClientId(clientId, current);
    }

    @Override
    public List<Rent> get() {
        return rentRepository.get();
    }

    @Override
    public Rent getById(UUID id) throws NotFoundException {
        Optional<Rent> rent = rentRepository.getById(id);
        if (rent.isEmpty()) {
            throw new NotFoundException("Rent with given id does not exist");
        }

        return rent.get();
    }

    @Override
    public void delete(UUID id) throws RentEndedException {
        Optional<Rent> rent = rentRepository.getById(id);
        if (rent.isPresent() && rent.get().getEndDate() != null) {
            throw new RentEndedException();
        }

        rentRepository.delete(id);
    }

    @Override
    public List<Rent> getByRealEstateID(UUID realEstateId, boolean current) {
        return rentRepository.getByRealEstateId(realEstateId, current);
    }
}
