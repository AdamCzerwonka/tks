package com.example.tks.adapter.data.repositories;

import com.example.tks.adapter.data.model.ManagerEnt;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManagerRepository {
    List<ManagerEnt> get();

    List<ManagerEnt> findManagersByLogin(String login);

    Optional<ManagerEnt> getById(UUID id);

    Optional<ManagerEnt> getByLogin(String login);

    ManagerEnt create(ManagerEnt manager) throws LoginAlreadyTakenException;

    ManagerEnt update(ManagerEnt manager) throws NotFoundException;
}
