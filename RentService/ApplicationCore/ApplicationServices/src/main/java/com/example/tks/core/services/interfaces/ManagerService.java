package com.example.tks.core.services.interfaces;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManagerService {
    List<Manager> get();

    List<Manager> findAllByLogin(String login);

    Manager getById(UUID id) throws NotFoundException;

    Manager getByLogin(String login) throws NotFoundException;

    Manager create(Manager manager) throws LoginAlreadyTakenException;

    Manager update(Manager manager) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
