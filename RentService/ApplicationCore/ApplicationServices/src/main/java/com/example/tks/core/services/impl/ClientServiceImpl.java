package com.example.tks.core.services.impl;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.services.interfaces.ClientService;
import com.example.tks.ports.infrastructure.ClientPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientPort clientRepository;

    @Override
    public List<Client> get() {
        return clientRepository.get();
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
    public Client create(Client client) {
        return clientRepository.create(client);
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
