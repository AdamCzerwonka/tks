package com.example.pasik.controllers;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.jws.Jws;
import com.example.pasik.managers.ClientManager;
import com.example.pasik.managers.RentManager;
import com.example.pasik.model.Error;
import com.example.pasik.model.Rent;
import com.example.pasik.model.dto.Client.ClientCreateRequest;
import com.example.pasik.model.dto.Client.ClientUpdateRequest;
import com.example.pasik.model.dto.Rent.RentResponse;
import com.example.pasik.model.dto.User.UserResponse;
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
    private final ClientManager clientManager;
    private final RentManager rentManager;
    private final Jws jws;

    public ClientController(final ClientManager clientManager, final RentManager rentManager, Jws jws) {
        this.clientManager = clientManager;
        this.rentManager = rentManager;
        this.jws = jws;
    }

    @GetMapping
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> get() {
        var result = clientManager.get().stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException, JOSEException {
        var result = clientManager.getById(id);
        var signed = jws.sign(result.getId().toString());

        return ResponseEntity.ok().header("Etag", signed).body(UserResponse.fromUser(result));
    }

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<RentResponse>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentManager.getByClientId(id, current).stream().map(RentResponse::fromRent).toList());
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<?> findClientsByLogin(@PathVariable String login) {
        var result = clientManager.findClientsByLogin(login).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<?> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = clientManager.getByLogin(login);

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ClientCreateRequest request) throws URISyntaxException, LoginAlreadyTakenException {
        var result = clientManager.create(request.ToClient());

        return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(UserResponse.fromUser(result));
    }

    @PutMapping
    public ResponseEntity<?> update(
            @RequestHeader(HttpHeaders.IF_MATCH) String token,
            @Valid @RequestBody ClientUpdateRequest request) throws NotFoundException {
        var isOk = jws.verifySign(token, request.getId());
        if (!isOk) {
            return ResponseEntity.badRequest().build();
        }
        var result = clientManager.update(request.ToClient());

        return ResponseEntity.ok(UserResponse.fromUser(result));
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
