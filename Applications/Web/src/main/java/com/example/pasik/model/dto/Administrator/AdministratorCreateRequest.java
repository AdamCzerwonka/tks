package com.example.pasik.model.dto.Administrator;

import com.example.pasik.model.Administrator;
import com.example.pasik.model.dto.User.UserCreateRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

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
