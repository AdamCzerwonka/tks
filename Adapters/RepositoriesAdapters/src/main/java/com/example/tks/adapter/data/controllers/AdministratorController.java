package com.example.tks.adapter.data.controllers;

import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.interfaces.AdministratorService;
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
    private final AdministratorService administratorService;
    private final Jws jws;

    public AdministratorController(final AdministratorService administratorService, Jws jws) {
        this.administratorService = administratorService;
        this.jws = jws;
    }

    @GetMapping
    public ResponseEntity<?> get() {
        var result = administratorService.get().stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        var result = administratorService.getById(id);

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<?> findAdministratorsByLogin(@PathVariable String login) {
        var result = administratorService.findAllByLogin(login).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<?> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = administratorService.getByLogin(login);

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AdministratorCreateRequest request) throws URISyntaxException {
        try {
            var result = administratorService.create(request.ToAdministrator());

            return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(UserResponse.fromUser(result));
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
        var result = administratorService.update(request.ToAdministrator());

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable UUID id) throws NotFoundException {
        administratorService.setActiveStatus(id, true);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) throws NotFoundException {
        administratorService.setActiveStatus(id, false);

        return ResponseEntity.ok().build();

    }
}
