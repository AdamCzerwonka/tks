package com.example.tks.rest.model.Manager;

import com.example.tks.adapter.data.model.dto.User.UserCreateRequest;
import com.example.tks.core.domain.model.Manager;
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
