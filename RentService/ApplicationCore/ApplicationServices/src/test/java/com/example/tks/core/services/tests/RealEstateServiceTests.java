package com.example.tks.core.services.tests;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.services.impl.RealEstateServiceImpl;
import com.example.tks.ports.infrastructure.RealEstatePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RealEstateServiceTests {
    @InjectMocks
    private RealEstateServiceImpl realEstateService;

    @Mock
    private RealEstatePort realEstatePort;

    List<RealEstate> realEstates;

    @BeforeEach
    public void beforeEach() {
        realEstates = new ArrayList<>();
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName1", "Random address 21/3", 12.5, 2000));
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName2", "Random address 21/4", 15.5, 3000));
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName3", "Random address 21/5", 20.5, 4000));
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName4", "Random address 21/6", 25.5, 5000));
    }

    @Test
    public void getShouldReturnRealEstates() {
        when(realEstatePort.get()).thenReturn(realEstates);
        Assertions.assertEquals(realEstates.size(), realEstateService.get().size());
    }

    @Test
    public void getByIdShouldReturnRealEstate() throws NotFoundException {
        UUID id = UUID.randomUUID();
        RealEstate realEstate = new RealEstate(id, "TestName", "Random address 21/3", 12.5, 2000);
        when(realEstatePort.getById(id)).thenReturn(Optional.of(realEstate));
        Assertions.assertEquals(realEstate, realEstateService.getById(id));
    }

    @Test
    public void getByIdShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(realEstatePort.getById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> realEstateService.getById(id));
    }

    @Test
    public void createShouldReturnRealEstate() {
        RealEstate realEstate = new RealEstate(UUID.randomUUID(), "TestName", "Random address 21/3", 12.5, 2000);
        when(realEstatePort.create(realEstate)).thenReturn(realEstate);
        Assertions.assertEquals(realEstate, realEstateService.create(realEstate));
    }

    @Test
    public void updateShouldReturnRealEstate() {
        RealEstate realEstate = new RealEstate(UUID.randomUUID(), "TestName", "Random address 21/3", 12.5, 2000);
        when(realEstatePort.update(realEstate)).thenReturn(realEstate);
        Assertions.assertEquals(realEstate, realEstateService.update(realEstate));
    }
}
