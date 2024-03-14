package com.example.pasik.repositories;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Administrator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdministratorRepository {
    List<Administrator> get();
    List<Administrator> findAdministratorsByLogin(String login);
    Optional<Administrator> getById(UUID id);
    Optional<Administrator> getByLogin(String login);
    Administrator create(Administrator administrator) throws LoginAlreadyTakenException;
    Administrator update(Administrator administrator) throws NotFoundException;
}
