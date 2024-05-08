package com.example.tks.core.services.tests;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.User;
import com.example.tks.core.services.impl.UserServiceImpl;
import com.example.tks.ports.infrastructure.UserPort;
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
public class UserServiceTests {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserPort userPort;

    @Mock
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    List<User> users;

    @BeforeEach
    public void beforeEach() {
        users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "user1", true, "ADMIN", "Password"));
        users.add(new User(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "user2", true, "ADMIN", "Password"));
        users.add(new User(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "user3", true, "ADMIN", "Password"));
    }

    @Test
    public void getShouldReturnUsers() {
        when(userPort.getAll("user")).thenReturn(users);
        Assertions.assertEquals(users.size(), userService.getAll("user").size());
    }

    @Test
    public void getByIdShouldReturnUser() throws NotFoundException {
        UUID id = UUID.randomUUID();
        User user = new User(id, "TestFirstName", "TestLastName", "user", true, "ADMIN", "Password");
        when(userPort.getById(id)).thenReturn(Optional.of(user));
        Assertions.assertEquals(user, userService.getById(id));
    }

    @Test
    public void getByIdShouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();
        when(userPort.getById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getById(id));
    }

    @Test
    public void getByLoginShouldReturnUser() throws NotFoundException {
        String login = "user";
        User user = new User(UUID.randomUUID(), "TestFirstName", "TestLastName", "user", true, "ADMIN", "Password");
        when(userPort.getByLogin(login)).thenReturn(Optional.of(user));
        Assertions.assertEquals(user, userService.getByLogin(login));
    }

    @Test
    public void getByLoginShouldThrowNotFoundException() {
        String login = "user";
        when(userPort.getByLogin(login)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getByLogin(login));
    }
}
