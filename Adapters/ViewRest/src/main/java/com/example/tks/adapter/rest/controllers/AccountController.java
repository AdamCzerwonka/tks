package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.aggregates.UserServiceAdapter;
import com.example.tks.adapter.rest.model.User.UpdatePasswordRequest;
import com.example.tks.adapter.rest.model.User.UserResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final UserServiceAdapter userServiceAdapter;
    private final JwtUtil jwtUtil;

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") @NotBlank @Valid String complexToken, @RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) throws NotFoundException {
        List<String> tokenArr = Arrays.stream(complexToken.split(" ")).toList();

        if (tokenArr.size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        String login = jwtUtil.getUserLogin(tokenArr.get(1));

        UserResponse result = userServiceAdapter.updatePassword(login, updatePasswordRequest);

        if (result == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
