package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.aggregates.ClientServiceAdapter;
import com.example.tks.adapter.rest.aggregates.RentServiceAdapter;
import com.example.tks.adapter.rest.model.Client.ClientCreateRequest;
import com.example.tks.adapter.rest.model.Client.ClientUpdateRequest;
import com.example.tks.adapter.rest.model.Rent.RentResponse;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping("/client")
public class ClientController {
    private final ClientServiceAdapter clientManager;
    private final RentServiceAdapter rentManager;
    private final Jws jws;

    public ClientController(final ClientServiceAdapter clientManager, final RentServiceAdapter rentManager, Jws jws) {
        this.clientManager = clientManager;
        this.rentManager = rentManager;
        this.jws = jws;
    }

    @GetMapping
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> get() {
        var result = clientManager.get();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException, JOSEException {
        var result = clientManager.getById(id);
        var signed = jws.sign(result.getId().toString());

        return ResponseEntity.ok().header("Etag", signed).body(result);
    }

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<RentResponse>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentManager.getByClientId(id, current));
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<?> findClientsByLogin(@PathVariable String login) {
        var result = clientManager.findAllByLogin(login);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<?> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = clientManager.getByLogin(login);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ClientCreateRequest request) throws URISyntaxException, LoginAlreadyTakenException {
        var result = clientManager.create(request);

        return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(result);
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestHeader(HttpHeaders.IF_MATCH) String token,
            @Valid @RequestBody ClientUpdateRequest request) throws NotFoundException {
        var isOk = jws.verifySign(token, request.getId());
        if (!isOk) {
            return ResponseEntity.badRequest().build();
        }
        var result = clientManager.update(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/activate/{id}")
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> activate(@PathVariable UUID id) throws NotFoundException {
        clientManager.setActiveStatus(id, true);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate/{id}")
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) throws NotFoundException {
        clientManager.setActiveStatus(id, false);

        return ResponseEntity.ok().build();

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginAlreadyTakenException.class)
    public Error handleLoginAlreadyTaken(LoginAlreadyTakenException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("loginTaken", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), errors);
    }
}
