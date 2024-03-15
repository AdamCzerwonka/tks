package com.example.tks.adapter.data.aggregates;

import com.example.tks.adapter.data.model.ManagerEnt;
import com.example.tks.adapter.data.repositories.ManagerRepository;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.Manager;
import com.example.tks.ports.infrastructure.ManagerPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ManagerRepositoryAdapter implements ManagerPort {
    private final ManagerRepository managerRepository;

    @Override
    public List<Manager> get() {
        List<ManagerEnt> result = managerRepository.get();
        return result.stream().map(ManagerEnt::toManager).toList();
    }

    @Override
    public List<Manager> findManagersByLogin(String login) {
        List<ManagerEnt> result = managerRepository.findManagersByLogin(login);
        return result.stream().map(ManagerEnt::toManager).toList();
    }

    @Override
    public Optional<Manager> getById(UUID id) {
        return managerRepository.getById(id).map(ManagerEnt::toManager);
    }

    @Override
    public Optional<Manager> getByLogin(String login) {
        return managerRepository.getByLogin(login).map(ManagerEnt::toManager);
    }

    @Override
    public Manager create(Manager manager) throws LoginAlreadyTakenException {
        ManagerEnt managerEnt = ManagerEnt.toManagerEnt(manager);
        return managerRepository.create(managerEnt).toManager();
    }

    @Override
    public Manager update(Manager manager) throws NotFoundException {
        ManagerEnt managerEnt = ManagerEnt.toManagerEnt(manager);
        return managerRepository.update(managerEnt).toManager();
    }
}
