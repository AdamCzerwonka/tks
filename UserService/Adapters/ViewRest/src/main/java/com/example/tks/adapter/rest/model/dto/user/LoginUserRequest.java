package com.example.tks.adapter.rest.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequest {
    @NotBlank(message = "Login cannot be blank")
    private String login;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
