package com.example.tks.adapter.soap.model.dto.Administrator;

import com.example.tks.adapter.soap.model.dto.User.UserCreateRequest;
import com.example.tks.core.domain.model.Administrator;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class AdministratorCreateRequest extends UserCreateRequest {
    @Builder
    public AdministratorCreateRequest(String firstName, String lastName, String login, Boolean active, String password) {
        super(firstName, lastName, login, active, password);
    }

    public Administrator ToAdministrator() {
        return new Administrator(null, getFirstName(), getLastName(), getLogin(), getActive(), getPassword());
    }
}
