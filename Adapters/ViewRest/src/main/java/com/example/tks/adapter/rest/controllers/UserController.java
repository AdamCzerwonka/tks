package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.aggregates.UserServiceAdapter;
import com.example.tks.adapter.rest.model.User.UserResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceAdapter userServiceAdapter;
    private final Jws jws;

    public UserController(final UserServiceAdapter userServiceAdapter, Jws jws) {
        this.userServiceAdapter = userServiceAdapter;
        this.jws = jws;
    }

    @GetMapping
    @RolesAllowed({"ADMINISTRATOR", "MANAGER"})
    public ResponseEntity<List<UserResponse>> get(@RequestParam(defaultValue = "") String filter) {
        var result = userServiceAdapter.getAll(filter);

        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) throws JOSEException {
        System.out.println(id);
        var result = userServiceAdapter.getById(id);
        var signed = jws.sign(result.get().getId().toString());
        return ResponseEntity.ok().header(HttpHeaders.ETAG, signed).body(result.get());
    }
}
