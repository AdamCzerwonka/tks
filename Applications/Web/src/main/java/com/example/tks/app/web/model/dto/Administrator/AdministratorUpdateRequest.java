package com.example.tks.app.web.model.dto.Administrator;

import com.example.tks.app.web.model.dto.User.UserUpdateRequest;
import com.example.tks.core.domain.model.Administrator;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AdministratorUpdateRequest extends UserUpdateRequest {
    @Builder
    public AdministratorUpdateRequest(UUID id, String firstName, String lastName, String login, Boolean active) {
        super(id, firstName, lastName, login, active);
    }

    public Administrator ToAdministrator() {
        return new Administrator(getId(), getFirstName(), getLastName(), getLogin(), getActive(), null);
    }
}
