package com.example.tks.adapter.rest.model.Client;

import com.example.tks.adapter.rest.model.User.UserUpdateRequest;
import com.example.tks.core.domain.model.Client;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
