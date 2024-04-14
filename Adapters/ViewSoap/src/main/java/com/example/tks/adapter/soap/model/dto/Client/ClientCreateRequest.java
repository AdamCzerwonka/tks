package com.example.tks.adapter.soap.model.dto.Client;

import com.example.tks.adapter.soap.model.dto.User.UserCreateRequest;
import com.example.tks.core.domain.model.Client;
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
public class ClientCreateRequest extends UserCreateRequest {
    @Builder
    public ClientCreateRequest(String firstName, String lastName, String login, Boolean active, String password) {
        super(firstName, lastName, login, active, password);
    }

    public Client ToClient() {
        return new Client(null, getFirstName(), getLastName(), getLogin(), getActive(), getPassword());
    }
}
