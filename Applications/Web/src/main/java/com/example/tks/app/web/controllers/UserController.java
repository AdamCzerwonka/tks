package com.example.tks.app.web.controllers;

import com.example.tks.app.web.model.dto.User.UserResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.Jws;
import com.example.tks.core.services.interfaces.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final Jws jws;


    @GetMapping
    @RolesAllowed({"ADMINISTRATOR", "MANAGER"})
    public ResponseEntity<List<UserResponse>> get(@RequestParam(defaultValue = "") String filter) {
        var result = userService.getAll(filter).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) throws JOSEException, NotFoundException {
        System.out.println(id);
        var result = userService.getById(id);
        var signed = jws.sign(result.getId().toString());
        return ResponseEntity.ok().header(HttpHeaders.ETAG, signed).body(UserResponse.fromUser(result));
    }
}
