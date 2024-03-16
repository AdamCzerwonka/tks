package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;

import java.util.List;
import java.util.UUID;

public interface ManagerServicePort<T, K> {
    List<T> get();

    List<T> findManagersByLogin(String login);

    T getById(UUID id) throws NotFoundException;

    T getByLogin(String login) throws NotFoundException;

    T create(K manager) throws LoginAlreadyTakenException;

    T update(K manager) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
