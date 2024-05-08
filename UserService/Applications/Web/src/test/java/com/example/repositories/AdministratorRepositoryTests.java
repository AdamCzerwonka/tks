package com.example.repositories;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.data.model.AdministratorEnt;
import com.example.tks.adapter.data.repositories.AdministratorRepository;
import com.example.tks.app.web.PasikApplication;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdministratorRepositoryTests extends ControllerTests {

    @Autowired
    AdministratorRepository administratorRepository;

    @BeforeEach
    void setup() {
        AdministratorEnt admin1 = new AdministratorEnt(UUID.randomUUID(), "FirstName1", "LastName1", "admin1", true, "ADMIN", "P@ssw0rd!");
        AdministratorEnt admin2 = new AdministratorEnt(UUID.randomUUID(), "FirstName2", "LastName2", "admin2", true, "ADMIN", "P@ssw0rd!");
        AdministratorEnt admin3 = new AdministratorEnt(UUID.randomUUID(), "FirstName3", "LastName3", "admin3", true, "ADMIN", "P@ssw0rd!");

        assertAll(
                () -> assertNotNull(administratorRepository.create(admin1)),
                () -> assertNotNull(administratorRepository.create(admin2)),
                () -> assertNotNull(administratorRepository.create(admin3)
                )
        );
    }

    @Test
    void createTest() {
        AdministratorEnt admin = new AdministratorEnt(UUID.randomUUID(), "FirstName0", "LastName0", "admin0", true, "ADMIN", "P@ssw0rd!");

        assertDoesNotThrow(() -> administratorRepository.create(admin));
    }

    @Test
    void getTest() {
        assertEquals(3, administratorRepository.get().size());
        AdministratorEnt admin = new AdministratorEnt(UUID.randomUUID(), "FirstName0", "LastName0", "admin0", true, "ADMIN", "P@ssw0rd!");
        assertDoesNotThrow(() -> administratorRepository.create(admin));
    }

    @Test
    void getByIdTest() {
        AdministratorEnt admin = new AdministratorEnt(null, "FirstName0", "LastName0", "admin0", true, "ADMIN", "P@ssw0rd!");

        assertDoesNotThrow(() -> administratorRepository.create(admin));
        assertNotNull(administratorRepository.getById(admin.getId()));
    }

    @Test
    void getByLoginTest() {
        String login = "admin0";
        AdministratorEnt admin = new AdministratorEnt(UUID.randomUUID(), "FirstName0", "LastName0", login, true, "ADMIN", "P@ssw0rd!");

        assertDoesNotThrow(() -> administratorRepository.create(admin));
        assertNotNull(administratorRepository.getByLogin(login));
    }

    @Test
    void findAllByLoginTest() {
        assertEquals(3, administratorRepository.findAllByLogin("admin").size());
    }

    @Test
    void updateTest() {
        AdministratorEnt admin = new AdministratorEnt(null, "FirstName0", "LastName0", "admin0", true, "ADMIN", "P@ssw0rd!");

        assertDoesNotThrow(() -> administratorRepository.create(admin));

        admin.setFirstName("FirstName1");
        admin.setLastName("LastName1");
        admin.setActive(false);

        assertDoesNotThrow(() -> administratorRepository.update(admin));

        AdministratorEnt updated = administratorRepository.getById(admin.getId()).get();

        assertEquals("FirstName1", updated.getFirstName());
        assertEquals("LastName1", updated.getLastName());
        assertFalse(updated.getActive());
    }
}
