package com.example.tks.adapter.rest.model.dto.user;

import com.example.tks.core.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Data
public class UserResponse implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String login;
    private Boolean active;
    private String role;

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getActive(), user.getRole());
    }
}
