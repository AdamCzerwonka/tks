package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.model.dto.user.UserResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.Jws;
import com.example.tks.core.services.interfaces.UserService;
import com.nimbusds.jose.JOSEException;
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
    public ResponseEntity<List<UserResponse>> get(@RequestParam(defaultValue = "") String filter) {
        var result = userService.getAll(filter).stream().map(UserResponse::fromUser).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) throws JOSEException, NotFoundException {
        var result = userService.getById(id);
        var signed = jws.sign(result.getId().toString());
        return ResponseEntity.ok().header(HttpHeaders.ETAG, signed).body(UserResponse.fromUser(result));
    }
}
