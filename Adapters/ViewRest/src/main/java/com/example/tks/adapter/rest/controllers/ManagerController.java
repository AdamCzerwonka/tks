package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.aggregates.ManagerServiceAdapter;
import com.example.tks.adapter.rest.model.Manager.ManagerCreateRequest;
import com.example.tks.adapter.rest.model.Manager.ManagerUpdateRequest;
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
@RequestMapping("/manager")
public class ManagerController {
    private final ManagerServiceAdapter managerManager;
    private final Jws jws;

    public ManagerController(final ManagerServiceAdapter managerManager, Jws jws) {
        this.managerManager = managerManager;
        this.jws = jws;
    }

    @GetMapping
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> get() {
        var result = managerManager.get();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        var result = managerManager.getById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<?> findClientsByLogin(@PathVariable String login) {
        var result = managerManager.findAllByLogin(login);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<?> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = managerManager.getByLogin(login);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ManagerCreateRequest request) throws URISyntaxException {
        try {
            var result = managerManager.create(request);

            return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(result);
        } catch (LoginAlreadyTakenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestHeader(HttpHeaders.IF_MATCH) String token,
            @Valid @RequestBody ManagerUpdateRequest request) throws NotFoundException {
        var isOk = jws.verifySign(token, request.getId());
        if (!isOk) {
            return ResponseEntity.badRequest().build();
        }
        var result = managerManager.update(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/activate/{id}")
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> activate(@PathVariable UUID id) throws NotFoundException {
        managerManager.setActiveStatus(id, true);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate/{id}")
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) throws NotFoundException {
        managerManager.setActiveStatus(id, false);

        return ResponseEntity.ok().build();
    }
}
