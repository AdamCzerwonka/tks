package com.example.tks.core.services.tests;

import com.example.tks.core.domain.exceptions.NotFoundException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdministratorServiceTests {
    @InjectMocks
    private AdministratorServiceImpl administratorService;

    @Mock
    private AdministratorPort administratorPort;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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

    @Test
    public void getByIdShouldReturnAdministrator() throws NotFoundException {
        UUID id = UUID.randomUUID();
        Administrator admin = new Administrator(id, "TestFirstName", "TestLastName", "admin", true, "Password");
        when(administratorPort.getById(id)).thenReturn(Optional.of(admin));

        Assertions.assertEquals(admin, administratorService.getById(id));
    }

    @Test
    public void getByIdShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(administratorPort.getById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> administratorService.getById(id));
    }

    @Test
    public void getByUsernameShouldReturnAdministrator() throws NotFoundException {
        String username = "admin";
        Administrator admin = new Administrator(UUID.randomUUID(), "TestFirstName", "TestLastName", username, true, "Password");
        when(administratorPort.getByLogin(username)).thenReturn(Optional.of(admin));

        Assertions.assertEquals(admin, administratorService.getByLogin(username));
    }

    @Test
    public void getByUsernameShouldThrowNotFoundException() {
        String username = "admin";
        when(administratorPort.getByLogin(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> administratorService.getByLogin(username));
    }

    @Test
    public void createShouldReturnAdministrator() throws Exception {
        Administrator admin = new Administrator(UUID.randomUUID(), "TestFirstName", "TestLastName", "admin", true, "Password");
        when(administratorPort.create(admin)).thenReturn(admin);

        Assertions.assertEquals(admin, administratorService.create(admin));
    }

    @Test
    public void updateShouldReturnAdministrator() throws NotFoundException {
        Administrator admin = new Administrator(UUID.randomUUID(), "TestFirstName", "TestLastName", "admin", true, "Password");
        when(administratorPort.update(admin)).thenReturn(admin);

        Assertions.assertEquals(admin, administratorService.update(admin));
    }
}