package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.AdministratorEnt;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdministratorRepository {
    List<AdministratorEnt> get();

    List<AdministratorEnt> findAdministratorsByLogin(String login);

    Optional<AdministratorEnt> getById(UUID id);

    Optional<AdministratorEnt> getByLogin(String login);

    AdministratorEnt create(AdministratorEnt administratorEnt) throws LoginAlreadyTakenException;

    AdministratorEnt update(AdministratorEnt administratorEnt) throws NotFoundException;
}
