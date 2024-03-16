package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.domain.exceptions.*;

import java.util.List;
import java.util.UUID;

public interface AdministratorServicePort<T, K> {
    List<T> get();

    List<T> findAdministratorsByLogin(String login);

    T getById(UUID id) throws NotFoundException;

    T getByLogin(String login) throws NotFoundException;

    T create(K administrator) throws LoginAlreadyTakenException;

    T update(K administrator) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
