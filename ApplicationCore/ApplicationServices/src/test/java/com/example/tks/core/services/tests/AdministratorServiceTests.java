package com.example.tks.core.services.tests;

import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.services.impl.AdministratorServiceImpl;
import com.example.tks.ports.infrastructure.AdministratorPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdministratorServiceTests {
    @InjectMocks
    private AdministratorServiceImpl administratorService;

    @Mock
    private AdministratorPort administratorPort;
    List<Administrator> admins;

    @BeforeEach
    public void beforeEach() {
        admins = new ArrayList<>();
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "admin1", true, "Password"));
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "admin2", true, "Password"));
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "admin3", true, "Password"));
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName4", "TestLastName4", "ad4", true, "Password"));
    }

    @Test
    public void getShouldReturnAdministrators() {
        when(administratorPort.get()).thenReturn(admins);

        Assertions.assertEquals(admins.size(), administratorService.get().size());
    }
}