package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.model.dto.client.ClientCreateRequest;
import com.example.tks.adapter.rest.model.dto.client.ClientResponse;
import com.example.tks.adapter.rest.model.dto.client.ClientUpdateRequest;
import com.example.tks.adapter.rest.model.dto.rent.RentResponse;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.interfaces.ClientService;
import com.example.tks.core.services.interfaces.RentService;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final RentService rentService;
    private final ClientService clientService;

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<RentResponse>> getClientRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentService.getByClientId(id, current).stream().map(RentResponse::fromRent).toList());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> get() {
        var result = clientService.get().stream().map(ClientResponse::fromClient).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getById(@PathVariable UUID id) throws NotFoundException {
        var result = clientService.getById(id);

        return ResponseEntity.ok().body(ClientResponse.fromClient(result));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientCreateRequest request) throws URISyntaxException, LoginAlreadyTakenException {
        var result = clientService.create(request.ToClient());

        return ResponseEntity.status(HttpStatus.CREATED).body(ClientResponse.fromClient(result));
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable UUID id) throws NotFoundException {
        clientService.setActiveStatus(id, true);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/deactivate/{id}")
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> deactivate(@PathVariable UUID id) throws NotFoundException {
        clientService.setActiveStatus(id, false);

        return ResponseEntity.ok().build();

    }
}
