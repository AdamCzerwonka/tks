package com.example.pasik.repositories;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Manager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManagerRepository {
    List<Manager> get();
    List<Manager> findManagersByLogin(String login);
    Optional<Manager> getById(UUID id);
    Optional<Manager> getByLogin(String login);
    Manager create(Manager manager) throws LoginAlreadyTakenException;
    Manager update(Manager manager) throws NotFoundException;
}
