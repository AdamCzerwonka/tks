package com.example.tks.adapter.data.aggregates;

import com.example.tks.adapter.data.model.AdministratorEnt;
import com.example.tks.adapter.data.repositories.AdministratorRepository;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.ports.infrastructure.AdministratorPort;
import com.example.tks.core.domain.model.Administrator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class AdministratorRepositoryAdapter implements AdministratorPort {
    private final AdministratorRepository repository;

    @Override
    public List<Administrator> get() {
        List<AdministratorEnt> result = repository.get();
        return result.stream().map(AdministratorEnt::toAdministrator).toList();
    }

    @Override
    public List<Administrator> findAdministratorsByLogin(String login) {
        List<AdministratorEnt> result = repository.findAdministratorsByLogin(login);
        return result.stream().map(AdministratorEnt::toAdministrator).toList();
    }

    @Override
    public Optional<Administrator> getById(UUID id) {
        Optional<AdministratorEnt> result = repository.getById(id);
        return result.map(AdministratorEnt::toAdministrator);
    }

    @Override
    public Optional<Administrator> getByLogin(String login) {
        Optional<AdministratorEnt> result = repository.getByLogin(login);
        return result.map(AdministratorEnt::toAdministrator);
    }

    @Override
    public Administrator update(Administrator administratorEnt) throws NotFoundException {
        AdministratorEnt administrator = repository.update(AdministratorEnt.toAdministratorEnt(administratorEnt));
        return administrator.toAdministrator();
    }

    @Override
    public Administrator create(Administrator administratorEnt) throws LoginAlreadyTakenException {
        AdministratorEnt administrator = repository.create(AdministratorEnt.toAdministratorEnt(administratorEnt));
        return administrator.toAdministrator();
    }
}
