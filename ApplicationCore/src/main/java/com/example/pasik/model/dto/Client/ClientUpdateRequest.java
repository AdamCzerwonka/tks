package com.example.pasik.model.dto.Client;

import com.example.pasik.model.Client;
import com.example.pasik.model.dto.User.UserUpdateRequest;
import lombok.*;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientUpdateRequest extends UserUpdateRequest {
    @Builder
    public ClientUpdateRequest(UUID id, String firstName, String lastName, String login, Boolean active) {
        super(id, firstName, lastName, login, active);
    }

    public Client ToClient() {
        return new Client(getId(), getFirstName(), getLastName(), getLogin(), getActive(), null);
    }
}
