package com.example.tks.adapter.soap.model.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlType(namespace = "https://www.example.com/user")
public class LoginUserRequest {
    @XmlElement
    @NotBlank(message = "Login cannot be blank")
    private String login;

    @XmlElement
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
