package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.model.dto.user.LoginUserRequest;
import com.example.tks.adapter.security.JwtService;
import com.example.tks.core.domain.model.User;
import com.example.tks.core.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest userReq) {
        try {
            User user = userService.getByLogin(userReq.getLogin());
            if (passwordEncoder.matches(userReq.getPassword(), user.getPassword())) {
                String token = jwtService.createToken(user.getId(), user.getLogin(), user.getRole());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
