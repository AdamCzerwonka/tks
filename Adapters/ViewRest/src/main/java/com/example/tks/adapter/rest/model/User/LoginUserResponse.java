package com.example.tks.adapter.rest.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponse {
    private String role;
    private String token;
}
