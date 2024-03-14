package com.example.pasik.controllers;

import com.example.pasik.jws.Jws;
import com.example.pasik.managers.UserManager;
import com.example.pasik.model.User;
import com.example.pasik.model.dto.User.UserResponse;
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
    private final UserManager userManager;
    private final Jws jws;

    public UserController(final UserManager userManager, Jws jws) {
        this.userManager = userManager;
        this.jws = jws;
    }

    @GetMapping
    @RolesAllowed({"ADMINISTRATOR", "MANAGER"})
    public ResponseEntity<List<UserResponse>> get(@RequestParam(defaultValue = "") String filter) {
        var result = userManager.getAll(filter).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) throws JOSEException {
        System.out.println(id);
        var result = userManager.getById(id);
        var signed = jws.sign(result.getId().toString());
        return ResponseEntity.ok().header(HttpHeaders.ETAG, signed).body(UserResponse.fromUser(result));
    }
}
