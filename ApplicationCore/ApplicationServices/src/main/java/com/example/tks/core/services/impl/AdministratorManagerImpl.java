package com.example.tks.core.services.impl;


import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.services.AdministratorManager;
import com.example.tks.ports.infrastructure.AdministratorPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdministratorManagerImpl implements AdministratorManager {
    private final AdministratorPort administratorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Administrator> get() {
        return administratorRepository.get();
    }

    @Override
    public List<Administrator> findAdministratorsByLogin(String login) {
        return administratorRepository.findAdministratorsByLogin(login);
    }

    @Override
    public Administrator getById(UUID id) throws NotFoundException {
        Optional<Administrator> administrator = administratorRepository.getById(id);

        if (administrator.isEmpty()) {
            throw new NotFoundException("Administrator with given id does not exists");
        }

        return administrator.get();
    }

    @Override
    public Administrator getByLogin(String login) throws NotFoundException {
        Optional<Administrator> administrator = administratorRepository.getByLogin(login);

        if (administrator.isEmpty()) {
            throw new NotFoundException("Administrator with given id does not exists");
        }

        return administrator.get();
    }

    @Override
    public Administrator create(Administrator administrator) throws LoginAlreadyTakenException {
        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        return administratorRepository.create(administrator);
    }

    @Override
    public Administrator update(Administrator administrator) throws NotFoundException {
        return administratorRepository.update(administrator);
    }

    @Override
    public void setActiveStatus(UUID id, boolean active) throws NotFoundException {
        Optional<Administrator> adminResult = administratorRepository.getById(id);
        if (adminResult.isEmpty()) {
            throw new NotFoundException("Administrator with given id does not exists");
        }

        Administrator admin = adminResult.get();
        admin.setActive(active);

        administratorRepository.update(admin);
    }
}
