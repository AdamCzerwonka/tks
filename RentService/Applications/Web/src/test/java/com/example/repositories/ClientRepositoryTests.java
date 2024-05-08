package com.example.repositories;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.adapter.data.repositories.ClientRepository;
import com.example.tks.app.web.PasikApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientRepositoryTests extends ControllerTests {

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        ClientEnt client1 = new ClientEnt(null, true);
        ClientEnt client2 = new ClientEnt(null, true);
        ClientEnt client3 = new ClientEnt(null, true);

        assertAll(
                () -> assertNotNull(clientRepository.create(client1)),
                () -> assertNotNull(clientRepository.create(client2)),
                () -> assertNotNull(clientRepository.create(client3)
                )
        );
    }

    @Test
    void createTest() {
        ClientEnt client = new ClientEnt(null, true);

        assertDoesNotThrow(() -> clientRepository.create(client));
    }

    @Test
    void getTest() {
        assertEquals(3, clientRepository.get().size());
        ClientEnt client = new ClientEnt(null, true);
        assertDoesNotThrow(() -> clientRepository.create(client));
    }

    @Test
    void getByIdTest() {
        ClientEnt client = new ClientEnt(null, true);

        assertDoesNotThrow(() -> clientRepository.create(client));
        assertNotNull(clientRepository.getById(client.getId()));
    }

    @Test
    void updateTest() {
        ClientEnt client = new ClientEnt(null, true);

        assertDoesNotThrow(() -> clientRepository.create(client));

        client.setActive(false);

        assertDoesNotThrow(() -> clientRepository.update(client));

        ClientEnt updated = clientRepository.getById(client.getId()).get();

        assertFalse(updated.isActive());
    }
}
