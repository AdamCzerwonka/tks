package com.example.pasik.repositories;

import com.example.pasik.controllers.ControllerTests;
import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.adapter.data.model.RealEstateEnt;
import com.example.tks.adapter.data.model.RentEnt;
import com.example.tks.adapter.data.repositories.RentRepository;
import com.example.tks.app.web.PasikApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RentRepositoryTests extends ControllerTests {

    @Autowired
    RentRepository rentRepository;

    RealEstateEnt realEstate1;
    ClientEnt client1;
    RentEnt rent1;

    @BeforeEach
    void setup() {
        realEstate1 = new RealEstateEnt(null, "Name1", "Address1", 100, 200);
        RealEstateEnt realEstate2 = new RealEstateEnt(null, "Name2", "Address2", 200, 300);
        RealEstateEnt realEstate3 = new RealEstateEnt(null, "Name3", "Address3", 300, 400);

        client1 = new ClientEnt(null, "FirstName1", "LastName1", "client1", true, "CLIENT", "P@ssw0rd!");
        ClientEnt client2 = new ClientEnt(null, "FirstName2", "LastName2", "client2", true, "CLIENT", "P@ssw0rd!");
        ClientEnt client3 = new ClientEnt(null, "FirstName3", "LastName3", "client3", true, "CLIENT", "P@ssw0rd!");


        rent1 = new RentEnt(null, client1, realEstate1, LocalDate.now(), LocalDate.now().plusDays(10));
        RentEnt rent2 = new RentEnt(null, client2, realEstate2, LocalDate.now(), LocalDate.now().plusDays(10));
        RentEnt rent3 = new RentEnt(null, client3, realEstate3, LocalDate.now(), LocalDate.now().plusDays(10));

        assertAll(
                () -> rentRepository.create(rent1),
                () -> rentRepository.create(rent2),
                () -> rentRepository.create(rent3)
        );
    }

    @Test
    void getTest() {
        assertEquals(3, rentRepository.get().size());
    }

    @Test
    void getByClientIdTest() {
        assertNotNull(rentRepository.getByClientId(client1.getId(), false));
    }

    @Test
    void getByRealEstateIdTest() {
        assertNotNull(rentRepository.getByRealEstateId(realEstate1.getId(), false));
    }

    @Test
    void createTest() {
        RealEstateEnt realEstate4 = new RealEstateEnt(null, "Name4", "Address4", 400, 500);
        ClientEnt client4 = new ClientEnt(null, "FirstName4", "LastName4", "client4", true, "CLIENT", "P@ssw0rd!");
        RentEnt rent4 = new RentEnt(null, client4, realEstate4, LocalDate.now(), LocalDate.now().plusDays(10));

        assertDoesNotThrow(() -> rentRepository.create(rent4));
    }

    @Test
    void updateTest() {
        LocalDate endDate = LocalDate.now().plusDays(20);
        rent1.setEndDate(endDate);
        assertDoesNotThrow(() -> rentRepository.update(rent1));
        assertEquals(endDate, rentRepository.getById(rent1.getId()).get().getEndDate());
    }

    @Test
    void deleteTest() {
        assertDoesNotThrow(() -> rentRepository.delete(rent1.getId()));
        assertEquals(2, rentRepository.get().size());
    }
}
