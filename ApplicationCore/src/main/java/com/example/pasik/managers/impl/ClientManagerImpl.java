package com.example.pasik.managers.impl;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.managers.ClientManager;
import com.example.pasik.model.Client;
import com.example.pasik.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientManagerImpl implements ClientManager {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Client> get() {
        return clientRepository.get();
    }

    @Override
    public List<Client> findClientsByLogin(String login) {
        return clientRepository.findClientsByLogin(login);
    }

    @Override
    public Client getById(UUID id) throws NotFoundException {
        Optional<Client> client = clientRepository.getById(id);

        if (client.isEmpty()) {
            throw new NotFoundException("Client with given id does not exists");
        }

        return client.get();
    }

    @Override
    public Client getByLogin(String login) throws NotFoundException {
        Optional<Client> client = clientRepository.getByLogin(login);

        if (client.isEmpty()) {
            throw new NotFoundException("Client with given id does not exists");
        }

        return client.get();
    }

    @Override
    public Client create(Client client) throws LoginAlreadyTakenException {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        return clientRepository.create(client);
    }

    @Override
    public Client update(Client client) throws NotFoundException {
        return clientRepository.update(client);
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {
        Optional<Client> clientResult = clientRepository.getById(id);
        if (clientResult.isEmpty()) {
            throw new NotFoundException("Client with given id does not exists");
        }

        Client client = clientResult.get();
        client.setActive(active);

        clientRepository.update(client);
    }
}
