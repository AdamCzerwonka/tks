package com.example.tks.adapter.soap.model.dto.Manager;

import com.example.tks.adapter.soap.model.dto.User.UserCreateRequest;
import com.example.tks.core.domain.model.Manager;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class ManagerCreateRequest extends UserCreateRequest {
    @Builder
    public ManagerCreateRequest(String firstName, String lastName, String login, Boolean active, String password) {
        super(firstName, lastName, login, active, password);
    }

    public Manager ToManager() {
        return new Manager(null, getFirstName(), getLastName(), getLogin(), getActive(), getPassword());
    }
}
