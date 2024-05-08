package com.example.tks.adapter.rest.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Old password cannot be blank")
    private String oldPassword;
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}
