package com.example.pasik.model.dto.Manager;

import com.example.pasik.model.Client;
import com.example.pasik.model.Manager;
import com.example.pasik.model.dto.User.UserCreateRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ManagerCreateRequest extends UserCreateRequest {
    @Builder
    public ManagerCreateRequest(String firstName, String lastName, String login, Boolean active, String password) {
        super(firstName, lastName, login, active, password);
    }

    public Manager ToManager() {
        return new Manager(null, getFirstName(), getLastName(), getLogin(), getActive(), getPassword());
    }
}
