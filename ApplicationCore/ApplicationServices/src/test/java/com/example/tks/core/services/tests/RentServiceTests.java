package com.example.tks.core.services.tests;

import com.example.tks.core.domain.exceptions.*;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.impl.RentServiceImpl;
import com.example.tks.ports.infrastructure.ClientPort;
import com.example.tks.ports.infrastructure.RealEstatePort;
import com.example.tks.ports.infrastructure.RentPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RentServiceTests {
    @InjectMocks
    private RentServiceImpl rentService;
    @Mock
    private RentPort rentPort;
    @Mock
    private ClientPort clientPort;
    @Mock
    private RealEstatePort realEstatePort;

    Rent rent;

    @BeforeEach
    public void beforeEach() {
        rent = new Rent(UUID.randomUUID(),
                new Client(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "client1", true, "Password"),
                new RealEstate(UUID.randomUUID(), "TestName1", "Random address 21/3", 12.5, 2000),
                LocalDate.now(),
                null);
    }

    @Test
    public void createShouldReturnRent() {
        when(clientPort.getById(rent.getClient().getId())).thenReturn(Optional.of(rent.getClient()));
        when(realEstatePort.getById(rent.getRealEstate().getId())).thenReturn(Optional.of(rent.getRealEstate()));
        when(rentPort.getByRealEstateId(rent.getRealEstate().getId(), true)).thenReturn(new ArrayList<>());
        Assertions.assertDoesNotThrow(() -> rentService.create(rent.getClient().getId(), rent.getRealEstate().getId(), LocalDate.now()));
    }

    @Test
    public void createShouldThrowNotFoundExceptionRealEstateIsEmpty() {
        when(realEstatePort.getById(rent.getRealEstate().getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> rentService.create(rent.getClient().getId(), rent.getRealEstate().getId(), LocalDate.now()));
    }

    @Test
    public void createShouldThrowNotFoundExceptionClientIsEmpty() {
        when(clientPort.getById(rent.getClient().getId())).thenReturn(Optional.empty());
        when(realEstatePort.getById(rent.getRealEstate().getId())).thenReturn(Optional.of(rent.getRealEstate()));
        Assertions.assertThrows(NotFoundException.class, () -> rentService.create(rent.getClient().getId(), rent.getRealEstate().getId(), LocalDate.now()));
    }

    @Test
    public void createShouldThrowAccountInactiveException() {
        Client newClient = new Client(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "client1", false, "Password");
        when(clientPort.getById(rent.getClient().getId())).thenReturn(Optional.of(newClient));
        when(realEstatePort.getById(rent.getRealEstate().getId())).thenReturn(Optional.of(rent.getRealEstate()));
        Assertions.assertThrows(AccountInactiveException.class, () -> rentService.create(rent.getClient().getId(), rent.getRealEstate().getId(), LocalDate.now()));
    }

    @Test
    public void createShouldThrowRealEstateRentedException() {
        ArrayList<Rent> rents = new ArrayList<>();
        rents.add(rent);
        when(clientPort.getById(rent.getClient().getId())).thenReturn(Optional.of(rent.getClient()));
        when(realEstatePort.getById(rent.getRealEstate().getId())).thenReturn(Optional.of(rent.getRealEstate()));
        when(rentPort.getByRealEstateId(rent.getRealEstate().getId(), true)).thenReturn(rents);
        Assertions.assertThrows(RealEstateRentedException.class, () -> rentService.create(rent.getClient().getId(), rent.getRealEstate().getId(), LocalDate.now()));
    }

    @Test
    public void endRentShouldThrowNotFoundException() {
        when(rentPort.getById(rent.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> rentService.endRent(rent.getId()));
    }

    @Test
    public void endRentShouldThrowRentEndedException() {
        rent.setEndDate(LocalDate.now());
        when(rentPort.getById(rent.getId())).thenReturn(Optional.of(rent));
        Assertions.assertThrows(RentEndedException.class, () -> rentService.endRent(rent.getId()));
    }

    @Test
    public void endRentShouldThrowInvalidEndRentDateException() {
        rent.setStartDate(LocalDate.now().plusYears(1));
        when(rentPort.getById(rent.getId())).thenReturn(Optional.of(rent));
        Assertions.assertThrows(InvalidEndRentDateException.class, () -> rentService.endRent(rent.getId()));
    }

    @Test
    public void endRentShouldReturnVoid() {
        when(rentPort.getById(rent.getId())).thenReturn(Optional.of(rent));
        rent.setEndDate(null);
        Assertions.assertDoesNotThrow(() -> rentService.endRent(rent.getId()));
    }

    @Test
    public void getByClientIdShouldReturnList() {
        when(rentPort.getByClientId(rent.getClient().getId(), true)).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(rentService.getByClientId(rent.getClient().getId(), true));
    }

    @Test
    public void getShouldReturnList() {
        when(rentPort.get()).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(rentService.get());
    }

    @Test
    public void getShouldReturnEmptyList() {
        when(rentPort.get()).thenReturn(new ArrayList<>());
        Assertions.assertTrue(rentService.get().isEmpty());
    }

    @Test
    public void getByIdShouldReturnRent() throws NotFoundException {
        when(rentPort.getById(rent.getId())).thenReturn(Optional.of(rent));
        Assertions.assertNotNull(rentService.getById(rent.getId()));
    }

    @Test
    public void getByIdShouldThrowNotFoundException() {
        when(rentPort.getById(rent.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> rentService.getById(rent.getId()));
    }

    @Test
    public void deleteShouldThrowRentEndedException() {
        rent.setEndDate(LocalDate.now());
        when(rentPort.getById(rent.getId())).thenReturn(Optional.of(rent));
        Assertions.assertThrows(RentEndedException.class, () -> rentService.delete(rent.getId()));
    }

    @Test
    public void deleteShouldReturnVoid() {
        rent.setEndDate(null);
        when(rentPort.getById(rent.getId())).thenReturn(Optional.of(rent));
        Assertions.assertDoesNotThrow(() -> rentService.delete(rent.getId()));
    }

    @Test
    public void getByRealEstateIDShouldReturnList() {
        when(rentPort.getByRealEstateId(rent.getRealEstate().getId(), true)).thenReturn(new ArrayList<>());
        Assertions.assertNotNull(rentService.getByRealEstateID(rent.getRealEstate().getId(), true));
    }

    @Test
    public void getByRealEstateIDShouldReturnEmptyList() {
        when(rentPort.getByRealEstateId(rent.getRealEstate().getId(), true)).thenReturn(new ArrayList<>());
        Assertions.assertTrue(rentService.getByRealEstateID(rent.getRealEstate().getId(), true).isEmpty());
    }
}
