package com.example.tks.adapter.rest.controllers;


import com.example.tks.adapter.rest.aggregates.AdministratorServiceAdapter;
import com.example.tks.adapter.rest.model.Administrator.AdministratorCreateRequest;
import com.example.tks.adapter.rest.model.Administrator.AdministratorUpdateRequest;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController()
@RequestMapping("/administrator")
@RolesAllowed("ADMINISTRATOR")
public class AdministratorController {
    private final AdministratorServiceAdapter administratorServiceAdapter;
    private final Jws jws;

    public AdministratorController(final AdministratorServiceAdapter administratorServiceAdapter, Jws jws) {
        this.administratorServiceAdapter = administratorServiceAdapter;
        this.jws = jws;
    }

    @GetMapping
    public ResponseEntity<?> get() {
        var result = administratorServiceAdapter.get();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        var result = administratorServiceAdapter.getById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<?> findAdministratorsByLogin(@PathVariable String login) {
        var result = administratorServiceAdapter.findAllByLogin(login);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<?> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = administratorServiceAdapter.getByLogin(login);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AdministratorCreateRequest request) throws URISyntaxException {
        try {
            var result = administratorServiceAdapter.create(request);

            return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(result);
        } catch (LoginAlreadyTakenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestHeader(HttpHeaders.IF_MATCH) String token,
            @Valid @RequestBody AdministratorUpdateRequest request) throws NotFoundException {
        var isOk = jws.verifySign(token, request.getId());
        if (!isOk) {
            return ResponseEntity.badRequest().build();
        }
        var result = administratorServiceAdapter.update(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable UUID id) throws NotFoundException {
        administratorServiceAdapter.setActiveStatus(id, true);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) throws NotFoundException {
        administratorServiceAdapter.setActiveStatus(id, false);

        return ResponseEntity.ok().build();

    }
}
