package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.model.dto.user.ChangePasswordRequest;
import com.example.tks.adapter.security.JwtService;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.model.User;
import com.example.tks.core.services.interfaces.UserService;
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
    private final UserService userService;
    private final JwtService jwtService;

    @PutMapping
    public ResponseEntity<Void> changePassword(@RequestHeader("Authorization") @NotBlank @Valid String complexToken, @RequestBody @Valid ChangePasswordRequest changePasswordRequest) throws NotFoundException {
        List<String> tokenArr = Arrays.stream(complexToken.split(" ")).toList();

        if (tokenArr.size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        String login = jwtService.getUserLogin(tokenArr.get(1));

        User result = userService.updatePassword(login, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());

        if (result == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
