package com.example.repositories;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.data.model.AdministratorEnt;
import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.adapter.data.model.ManagerEnt;
import com.example.tks.adapter.data.repositories.AdministratorRepository;
import com.example.tks.adapter.data.repositories.ClientRepository;
import com.example.tks.adapter.data.repositories.ManagerRepository;
import com.example.tks.adapter.data.repositories.UserRepository;
import com.example.tks.app.web.PasikApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTests extends ControllerTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdministratorRepository administratorRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ClientRepository clientRepository;

    @BeforeEach
    void setup() {
        AdministratorEnt admin1 = new AdministratorEnt(UUID.randomUUID(), "FirstName1", "LastName1", "admin1", true, "ADMIN", "P@ssw0rd!");
        ManagerEnt manager1 = new ManagerEnt(null, "FirstName1", "LastName1", "manager1", true, "MANAGER", "P@ssw0rd!");
        ClientEnt client1 = new ClientEnt(null, "FirstName1", "LastName1", "client1", true, "CLIENT", "P@ssw0rd!");

        assertAll(
                () -> assertNotNull(administratorRepository.create(admin1)),
                () -> assertNotNull(managerRepository.create(manager1)),
                () -> assertNotNull(clientRepository.create(client1)
                )
        );
    }

    @Test
    void getAllTest() {
        assertEquals(3, userRepository.getAll("1").size());
    }

    @Test
    void getByIdTest() {
        ClientEnt client = new ClientEnt(null, "FirstName0", "LastName0", "client0", true, "CLIENT", "P@ssw0rd!");

        assertDoesNotThrow(() -> clientRepository.create(client));
        assertNotNull(userRepository.getById(client.getId()));
    }

    @Test
    void getByLoginTest() {
        ClientEnt client = new ClientEnt(null, "FirstName0", "LastName0", "client0", true, "CLIENT", "P@ssw0rd!");

        assertDoesNotThrow(() -> clientRepository.create(client));
        assertNotNull(userRepository.getByLogin(client.getLogin()));
    }

    @Test
    void updatePasswordTest() {
        ClientEnt client = new ClientEnt(null, "FirstName0", "LastName0", "client0", true, "CLIENT", "P@ssw0rd!");

        assertDoesNotThrow(() -> clientRepository.create(client));
        assertDoesNotThrow(() -> userRepository.updatePassword(client.getLogin(), "newPassword"));
        assertEquals("newPassword", userRepository.getById(client.getId()).get().getPassword());
    }
}
