package com.example.tks.adapter.rest.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserCreateRequest {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotBlank(message = "Login cannot be blank")
    private String login;
    @NotNull
    private Boolean active;
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
