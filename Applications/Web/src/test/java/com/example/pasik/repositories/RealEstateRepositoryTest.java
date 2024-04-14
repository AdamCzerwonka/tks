package com.example.pasik.repositories;

import com.example.pasik.controllers.ControllerTests;
import com.example.tks.adapter.data.model.RealEstateEnt;
import com.example.tks.adapter.data.repositories.RealEstateRepository;
import com.example.tks.app.web.PasikApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RealEstateRepositoryTest extends ControllerTests {

    @Autowired
    RealEstateRepository realEstateRepository;

    @BeforeEach
    void setup() {
        RealEstateEnt realEstate1 = new RealEstateEnt(null, "Name1", "Address1", 100, 200);
        RealEstateEnt realEstate2 = new RealEstateEnt(null, "Name2", "Address2", 200, 300);
        RealEstateEnt realEstate3 = new RealEstateEnt(null, "Name3", "Address3", 300, 400);

        assertAll(
                () -> assertNotNull(realEstateRepository.create(realEstate1)),
                () -> assertNotNull(realEstateRepository.create(realEstate2)),
                () -> assertNotNull(realEstateRepository.create(realEstate3)
                )
        );
    }

    @Test
    void createTest() {
        RealEstateEnt realEstate = new RealEstateEnt(null, "Name0", "Address0", 100, 100);

        assertNotNull(realEstateRepository.create(realEstate));
    }

    @Test
    void getTest() {
        assertEquals(3, realEstateRepository.get().size());
    }

    @Test
    void getByIdTest() {
        RealEstateEnt realEstate = new RealEstateEnt(null, "Name0", "Address0", 100, 100);

        assertDoesNotThrow(() -> realEstateRepository.create(realEstate));
        assertNotNull(realEstateRepository.getById(realEstate.getId()));
    }

    @Test
    void updateTest() {
        RealEstateEnt realEstate = new RealEstateEnt(null, "Name0", "Address0", 0, 0);
        realEstate = realEstateRepository.create(realEstate);

        realEstate.setName("NameUpdated");
        realEstate.setAddress("AddressUpdated");
        realEstate.setPrice(100);
        realEstate.setArea(200);

        realEstateRepository.update(realEstate);

        RealEstateEnt updated = realEstateRepository.getById(realEstate.getId()).get();

        assertEquals("NameUpdated", updated.getName());
        assertEquals("AddressUpdated", updated.getAddress());
        assertEquals(100, updated.getPrice());
        assertEquals(200, updated.getArea());
    }
}
