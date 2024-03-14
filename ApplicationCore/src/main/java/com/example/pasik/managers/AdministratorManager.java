package com.example.pasik.managers;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.model.Administrator;

import java.util.List;
import java.util.UUID;

public interface AdministratorManager {
    List<Administrator> get();
    List<Administrator> findAdministratorsByLogin(String login);
    Administrator getById(UUID id) throws NotFoundException;
    Administrator getByLogin(String login) throws NotFoundException;
    Administrator create(Administrator administrator) throws LoginAlreadyTakenException;
    Administrator update(Administrator administrator) throws NotFoundException;
    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
