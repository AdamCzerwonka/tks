package com.example.tks.core.services.tests;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;
import com.example.tks.core.services.impl.ManagerServiceImpl;
import com.example.tks.ports.infrastructure.ManagerPort;
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
public class ManagerServiceTests {
    @InjectMocks
    private ManagerServiceImpl managerService;
    @Mock
    private ManagerPort managerPort;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    List<Manager> managers;

    @BeforeEach
    public void beforeEach() {
        managers = new ArrayList<>();
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "manager1", true, "Password"));
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "manager2", true, "Password"));
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "manager3", true, "Password"));
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName4", "TestLastName4", "mngr4", true, "Password"));
    }

    @Test
    public void getShouldReturnManagers() {
        when(managerPort.get()).thenReturn(managers);
        Assertions.assertEquals(managers.size(), managerService.get().size());
    }

    @Test
    public void getByIdShouldReturnManager() throws NotFoundException {
        UUID id = UUID.randomUUID();
        Manager manager = new Manager(id, "TestFirstName", "TestLastName", "manager", true, "Password");
        when(managerPort.getById(id)).thenReturn(Optional.of(manager));
        Assertions.assertEquals(manager, managerService.getById(id));
    }

    @Test
    public void getByIdShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(managerPort.getById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> managerService.getById(id));
    }

    @Test
    public void getByIdShouldNotThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        Manager manager = new Manager(id, "TestFirstName", "TestLastName", "manager", true, "Password");
        when(managerPort.getById(id)).thenReturn(Optional.of(manager));
        Assertions.assertDoesNotThrow(() -> managerService.getById(id));
    }

    @Test
    public void getByUsernameShouldReturnManager() throws NotFoundException {
        String username = "manager";
        Manager manager = new Manager(UUID.randomUUID(), "TestFirstName", "TestLastName", username, true, "Password");
        when(managerPort.getByLogin(username)).thenReturn(Optional.of(manager));
        Assertions.assertEquals(manager, managerService.getByLogin(username));
    }

    @Test
    public void createShouldReturnManager() throws Exception {
        Manager manager = new Manager(UUID.randomUUID(), "TestFirstName", "TestLastName", "manager", true, "Password");
        when(managerPort.create(manager)).thenReturn(manager);
        Assertions.assertEquals(manager, managerService.create(manager));
    }

    @Test
    public void updateShouldReturnManager() throws NotFoundException {
        UUID id = UUID.randomUUID();
        Manager manager = new Manager(id, "TestFirstName", "TestLastName", "manager", true, "Password");
        when(managerPort.update(manager)).thenReturn(manager);
        Assertions.assertEquals(manager, managerService.update(manager));
    }
}
