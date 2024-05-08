package com.example.tks.core.services.tests;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.services.impl.ClientServiceImpl;
import com.example.tks.ports.infrastructure.ClientPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTests {
    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientPort clientPort;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    List<Client> clients;

    @BeforeEach
    public void beforeEach() {
        clients = new ArrayList<>();
        clients.add(new Client(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "client1", true, "Password"));
        clients.add(new Client(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "client2", true, "Password"));
        clients.add(new Client(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "client3", true, "Password"));
        clients.add(new Client(UUID.randomUUID(), "TestFirstName4", "TestLastName4", "cl4", true, "Password"));
    }

    @Test
    public void getShouldReturnClients() {
        when(clientPort.get()).thenReturn(clients);

        Assertions.assertEquals(clients.size(), clientService.get().size());
    }

    @Test
    public void getByIdShouldReturnClient() throws NotFoundException {
        UUID id = UUID.randomUUID();
        Client client = new Client(id, "TestFirstName", "TestLastName", "client", true, "Password");
        when(clientPort.getById(id)).thenReturn(Optional.of(client));

        Assertions.assertEquals(client, clientService.getById(id));
    }

    @Test
    public void getByIdShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(clientPort.getById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(Exception.class, () -> clientService.getById(id));
    }

    @Test
    public void getByUsernameShouldReturnClient() throws NotFoundException {
        String username = "client";
        Client client = new Client(UUID.randomUUID(), "TestFirstName", "TestLastName", username, true, "Password");
        when(clientPort.getByLogin(username)).thenReturn(Optional.of(client));

        Assertions.assertEquals(client, clientService.getByLogin(username));
    }

    @Test
    public void getByUsernameShouldThrowNotFoundException() {
        String username = "client";
        when(clientPort.getByLogin(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(Exception.class, () -> clientService.getByLogin(username));
    }

    @Test
    public void createShouldReturnClient() throws Exception {
        Client client = new Client(UUID.randomUUID(), "TestFirstName", "TestLastName", "client", true, "Password");
        when(clientPort.create(client)).thenReturn(client);

        Assertions.assertEquals(client, clientService.create(client));
    }

    @Test
    public void updateShouldReturnClient() throws Exception {
        Client client = new Client(UUID.randomUUID(), "TestFirstName", "TestLastName", "client", true, "Password");
        when(clientPort.update(client)).thenReturn(client);

        Assertions.assertEquals(client, clientService.update(client));
    }
}
