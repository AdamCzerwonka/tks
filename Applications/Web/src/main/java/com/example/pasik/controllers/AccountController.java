package com.example.pasik.controllers;

import com.example.pasik.auth.JwtUtil;
import com.example.pasik.managers.UserManager;
import com.example.pasik.model.User;
import com.example.pasik.model.dto.User.ChangePasswordRequest;
import jakarta.annotation.security.RolesAllowed;
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
    private final UserManager userManager;
    private final JwtUtil jwtUtil;

    @PutMapping
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") @NotBlank @Valid String complexToken, @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
        List<String> tokenArr = Arrays.stream(complexToken.split(" ")).toList();

        if (tokenArr.size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        String login = jwtUtil.getUserLogin(tokenArr.get(1));

        User result = userManager.updatePassword(login, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());

        if (result == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
