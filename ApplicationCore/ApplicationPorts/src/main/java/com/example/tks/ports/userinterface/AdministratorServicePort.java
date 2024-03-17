package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdministratorServicePort<T, C, U> {
    List<T> get();

    List<T> findAllByLogin(String login);

    T getById(UUID id) throws NotFoundException;

    T getByLogin(String login) throws NotFoundException;

    T create(C administratorCreateRequest) throws LoginAlreadyTakenException;

    T update(U administratorUpdateRequest) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
