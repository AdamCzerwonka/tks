package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ManagerServicePort<T, C, U> {
    List<T> get();

    List<T> findAllByLogin(String login);

    T getById(UUID id) throws NotFoundException;

    T getByLogin(String login) throws NotFoundException;

    T create(C managerCreateRequest) throws LoginAlreadyTakenException;

    T update(U managerUpdateRequest) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
