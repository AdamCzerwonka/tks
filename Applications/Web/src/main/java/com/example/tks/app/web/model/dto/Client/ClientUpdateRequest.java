package com.example.tks.app.web.model.dto.Client;

import com.example.tks.core.domain.model.Client;
import com.example.tks.app.web.model.dto.User.UserUpdateRequest;
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
