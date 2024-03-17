package com.example.tks.adapter.rest.model.Administrator;

import com.example.tks.adapter.rest.model.User.UserCreateRequest;
import com.example.tks.core.domain.model.Administrator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AdministratorCreateRequest extends UserCreateRequest {
    @Builder
    public AdministratorCreateRequest(String firstName, String lastName, String login, Boolean active, String password) {
        super(firstName, lastName, login, active, password);
    }

    public Administrator ToAdministrator() {
        return new Administrator(null, getFirstName(), getLastName(), getLogin(), getActive(), getPassword());
    }
}
