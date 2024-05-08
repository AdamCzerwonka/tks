package com.example.repositories;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.data.model.ManagerEnt;
import com.example.tks.adapter.data.repositories.ManagerRepository;
import com.example.tks.app.web.PasikApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ManagerRepositoryTests extends ControllerTests {

    @Autowired
    ManagerRepository managerRepository;

    @BeforeEach
    void setup() {
        ManagerEnt manager1 = new ManagerEnt(null, "FirstName1", "LastName1", "manager1", true, "MANAGER", "P@ssw0rd!");
        ManagerEnt manager2 = new ManagerEnt(null, "FirstName2", "LastName2", "manager2", true, "MANAGER", "P@ssw0rd!");
        ManagerEnt manager3 = new ManagerEnt(null, "FirstName3", "LastName3", "manager3", true, "MANAGER", "P@ssw0rd!");

        assertAll(
                () -> assertNotNull(managerRepository.create(manager1)),
                () -> assertNotNull(managerRepository.create(manager2)),
                () -> assertNotNull(managerRepository.create(manager3)
                )
        );
    }

    @Test
    void createTest() {
        ManagerEnt manager = new ManagerEnt(null, "FirstName0", "LastName0", "manager0", true, "MANAGER", "P@ssw0rd!");

        assertDoesNotThrow(() -> managerRepository.create(manager));
    }

    @Test
    void getTest() {
        assertEquals(3, managerRepository.get().size());
        ManagerEnt manager = new ManagerEnt(null, "FirstName0", "LastName0", "manager0", true, "MANAGER", "P@ssw0rd!");
        assertDoesNotThrow(() -> managerRepository.create(manager));
    }

    @Test
    void getByIdTest() {
        ManagerEnt manager = new ManagerEnt(null, "FirstName0", "LastName0", "manager0", true, "MANAGER", "P@ssw0rd!");

        assertDoesNotThrow(() -> managerRepository.create(manager));
        assertNotNull(managerRepository.getById(manager.getId()));
    }

    @Test
    void getByLoginTest() {
        ManagerEnt manager = new ManagerEnt(null, "FirstName0", "LastName0", "manager0", true, "MANAGER", "P@ssw0rd!");

        assertDoesNotThrow(() -> managerRepository.create(manager));
        assertNotNull(managerRepository.getByLogin(manager.getLogin()));
    }

    @Test
    void findAllByLoginTest() {
        assertEquals(3, managerRepository.findAllByLogin("manager").size());
    }

    @Test
    void updateTest() {
        ManagerEnt manager = new ManagerEnt(null, "FirstName0", "LastName0", "manager0", true, "MANAGER", "P@ssw0rd!");

        assertDoesNotThrow(() -> managerRepository.create(manager));
        manager.setFirstName("FirstName1");
        manager.setLastName("LastName1");
        manager.setActive(false);

        assertDoesNotThrow(() -> managerRepository.update(manager));

        ManagerEnt updated = managerRepository.getById(manager.getId()).get();

        assertEquals("FirstName1", updated.getFirstName());
        assertEquals("LastName1", updated.getLastName());
        assertFalse(updated.getActive());
    }
}
