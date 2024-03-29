package com.example.tks.app.web.controllers;

import com.example.tks.app.web.model.dto.Client.ClientUpdateRequest;
import com.example.tks.app.web.model.dto.Rent.RentResponse;
import com.example.tks.core.domain.exceptions.LoginAlreadyTakenException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.Jws;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.tks.app.web.model.dto.User.UserResponse;
import com.example.tks.app.web.model.dto.Client.ClientCreateRequest;
import com.example.tks.app.web.model.Error;

@RestController()
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final RentService rentService;
    private final Jws jws;


    @GetMapping
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<List<UserResponse>> get() {
        var result = clientService.get().stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) throws NotFoundException, JOSEException {
        var result = clientService.getById(id);
        var signed = jws.sign(result.getId().toString());

        return ResponseEntity.ok().header("Etag", signed).body(UserResponse.fromUser(result));
    }

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<RentResponse>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentService.getByClientId(id, current).stream().map(RentResponse::fromRent).toList());
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<List<UserResponse>> findClientsByLogin(@PathVariable String login) {
        var result = clientService.findAllByLogin(login).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<UserResponse> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = clientService.getByLogin(login);

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody ClientCreateRequest request) throws URISyntaxException, LoginAlreadyTakenException {
        var result = clientService.create(request.ToClient());

        return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(UserResponse.fromUser(result));
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(
            @RequestHeader(HttpHeaders.IF_MATCH) String token,
            @Valid @RequestBody ClientUpdateRequest request) throws NotFoundException {
        var isOk = jws.verifySign(token, request.getId());
        if (!isOk) {
            return ResponseEntity.badRequest().build();
        }
        var result = clientService.update(request.ToClient());

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @PostMapping("/activate/{id}")
    @RolesAllowed("ADMINISTRATOR")
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginAlreadyTakenException.class)
    public Error handleLoginAlreadyTaken(LoginAlreadyTakenException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("loginTaken", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), errors);
    }
}
