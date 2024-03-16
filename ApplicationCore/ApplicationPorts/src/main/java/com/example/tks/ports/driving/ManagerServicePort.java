package com.example.tks.ports.driving;

import java.util.List;
import java.util.UUID;

public interface ManagerServicePort {
    List<Manager> get();

    List<Manager> findManagersByLogin(String login);

    Manager getById(UUID id) throws NotFoundException;

    Manager getByLogin(String login) throws NotFoundException;

    Manager create(Manager manager) throws LoginAlreadyTakenException;

    Manager update(Manager manager) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
