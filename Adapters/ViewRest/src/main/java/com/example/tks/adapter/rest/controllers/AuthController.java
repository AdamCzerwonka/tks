package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.aggregates.UserServiceAdapter;
import com.example.tks.adapter.rest.model.User.LoginUserRequest;
import com.example.tks.core.domain.model.User;
import com.example.tks.core.services.JwtService;
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
    private final UserServiceAdapter userServiceAdapter;
    private final JwtService jwtUtil;
    private final PasswordEncoder passwordEncoder;


    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest userReq) {
        try {
            User user = userServiceAdapter.getByLogin(userReq);
            if (passwordEncoder.matches(userReq.getPassword(), user.getPassword())) {
                String token = jwtUtil.createToken(user);
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
