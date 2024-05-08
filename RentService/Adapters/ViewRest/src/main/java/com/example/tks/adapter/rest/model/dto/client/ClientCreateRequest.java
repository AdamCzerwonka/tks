package com.example.tks.adapter.rest.model.dto.client;

import com.example.tks.adapter.rest.model.dto.user.UserCreateRequest;
import com.example.tks.core.domain.model.Client;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ClientCreateRequest extends UserCreateRequest {
    @Builder
    public ClientCreateRequest(String firstName, String lastName, String login, Boolean active, String password) {
        super(firstName, lastName, login, active, password);
    }

    public Client ToClient() {
        return new Client(null, getFirstName(), getLastName(), getLogin(), getActive(), getPassword());
    }
}
