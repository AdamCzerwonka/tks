package com.example.tks.core.services.impl;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;
import com.example.tks.ports.infrastructure.ManagerPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.tks.core.services.ManagerManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ManagerManagerImpl implements ManagerManager {
    private final ManagerPort managerRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<Manager> get() {
        return managerRepository.get();
    }

    @Override
    public List<Manager> findManagersByLogin(String login) {
        return managerRepository.findManagersByLogin(login);
    }

    @Override
    public Manager getById(UUID id) throws NotFoundException {
        Optional<Manager> manager = managerRepository.getById(id);

        if (manager.isEmpty()) {
            throw new NotFoundException("Manager with given id does not exists");
        }

        return manager.get();
    }

    @Override
    public Manager getByLogin(String login) throws NotFoundException {
        Optional<Manager> manager = managerRepository.getByLogin(login);

        if (manager.isEmpty()) {
            throw new NotFoundException("Manager with given id does not exists");
        }

        return manager.get();
    }

    @Override
    public Manager create(Manager manager) throws LoginAlreadyTakenException {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        return managerRepository.create(manager);
    }

    @Override
    public Manager update(Manager manager) throws NotFoundException {
        return managerRepository.update(manager);
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {
        Optional<Manager> managerResult = managerRepository.getById(id);
        if (managerResult.isEmpty()) {
            throw new RuntimeException("Manager with given id does not exists");
        }

        Manager manager = managerResult.get();
        manager.setActive(active);

        managerRepository.update(manager);
    }
}
