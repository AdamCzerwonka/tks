package com.example.tks.ports.userinterface;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientServicePort<T, K> {
    List<T> get();

    List<T> findClientsByLogin(String login);

    T getById(UUID id) throws NotFoundException;

    T getByLogin(String login) throws NotFoundException;

    T create(K client) throws LoginAlreadyTakenException;

    T update(K client) throws NotFoundException;

    void setActiveStatus(UUID id, boolean active) throws NotFoundException;
}
