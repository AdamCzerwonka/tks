package com.example.tks.adapter.data.aggregates;

import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.adapter.data.repositories.ClientRepository;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;
import com.example.tks.ports.infrastructure.ClientPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ClientRepositoryAdapter implements ClientPort {
    private final ClientRepository clientRepository;

    @Override
    public List<Client> get() {
        List<ClientEnt> result = clientRepository.get();
        return result.stream().map(ClientEnt::toClient).toList();
    }

    @Override
    public List<Client> findClientsByLogin(String login) {
        List<ClientEnt> result = clientRepository.findClientsByLogin(login);
        return result.stream().map(ClientEnt::toClient).toList();
    }

    @Override
    public Optional<Client> getById(UUID id) {
        Optional<ClientEnt> result = clientRepository.getById(id);
        return result.map(ClientEnt::toClient);
    }

    @Override
    public Optional<Client> getByLogin(String login) {
        Optional<ClientEnt> result = clientRepository.getByLogin(login);
        return result.map(ClientEnt::toClient);
    }

    @Override
    public Client create(Client client) throws LoginAlreadyTakenException {
        ClientEnt clientEnt = ClientEnt.toClientEnt(client);
        ClientEnt result = clientRepository.create(clientEnt);
        return result.toClient();
    }

    @Override
    public Client update(Client client) throws NotFoundException {
        ClientEnt clientEnt = ClientEnt.toClientEnt(client);
        ClientEnt result = clientRepository.update(clientEnt);
        return result.toClient();
    }
}
