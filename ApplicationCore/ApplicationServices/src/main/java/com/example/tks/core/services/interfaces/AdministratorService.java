package com.example.tks.core.services.interfaces;

import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.domain.exceptions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdministratorService {
    List<Administrator> get();

    List<Administrator> findAllByLogin(String login);

    Administrator getById(UUID id) throws NotFoundException;

    Administrator getByLogin(String login) throws NotFoundException;

    Administrator create(Administrator administrator) throws LoginAlreadyTakenException;

    Administrator update(Administrator administrator) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
