package com.example.tks.adapter.soap.model.dto.Administrator;

import com.example.tks.adapter.soap.model.dto.User.UserUpdateRequest;
import com.example.tks.core.domain.model.Administrator;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministratorUpdateRequest extends UserUpdateRequest {
    @Builder
    public AdministratorUpdateRequest(UUID id, String firstName, String lastName, String login, Boolean active) {
        super(id, firstName, lastName, login, active);
    }

    public Administrator ToAdministrator() {
        return new Administrator(getId(), getFirstName(), getLastName(), getLogin(), getActive(), null);
    }
}
