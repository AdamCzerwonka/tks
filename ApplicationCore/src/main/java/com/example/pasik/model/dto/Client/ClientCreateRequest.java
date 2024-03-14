package com.example.pasik.model.dto.Client;

import com.example.pasik.model.Client;
import com.example.pasik.model.dto.User.UserCreateRequest;
import lombok.*;

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
