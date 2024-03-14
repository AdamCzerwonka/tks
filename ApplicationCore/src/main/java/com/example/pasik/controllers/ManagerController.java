package com.example.pasik.controllers;

import com.example.pasik.exceptions.LoginAlreadyTakenException;
import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.jws.Jws;
import com.example.pasik.managers.ManagerManager;
import com.example.pasik.model.dto.Client.ClientCreateRequest;
import com.example.pasik.model.dto.Manager.ManagerCreateRequest;
import com.example.pasik.model.dto.Manager.ManagerUpdateRequest;
import com.example.pasik.model.dto.User.UserResponse;
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
    private final ManagerManager managerManager;
    private final Jws jws;

    public ManagerController(final ManagerManager managerManager, Jws jws) {
        this.managerManager = managerManager;
        this.jws = jws;
    }

    @GetMapping
    @RolesAllowed("ADMINISTRATOR")
    public ResponseEntity<?> get() {
        var result = managerManager.get().stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        var result = managerManager.getById(id);

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @GetMapping("/login/many/{login}")
    public ResponseEntity<?> findClientsByLogin(@PathVariable String login) {
        var result = managerManager.findManagersByLogin(login).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/login/single/{login}")
    public ResponseEntity<?> getByLogin(@PathVariable String login) throws NotFoundException {
        var result = managerManager.getByLogin(login);

        return ResponseEntity.ok(UserResponse.fromUser(result));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ManagerCreateRequest request) throws URISyntaxException {
        try {
            var result = managerManager.create(request.ToManager());

            return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(UserResponse.fromUser(result));
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
        var result = managerManager.update(request.ToManager());

        return ResponseEntity.ok(UserResponse.fromUser(result));
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
