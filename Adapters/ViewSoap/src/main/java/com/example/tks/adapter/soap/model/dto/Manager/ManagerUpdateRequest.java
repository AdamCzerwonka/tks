package com.example.tks.adapter.soap.model.dto.Manager;

import com.example.tks.adapter.soap.model.dto.User.UserUpdateRequest;
import com.example.tks.core.domain.model.Manager;
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
public class ManagerUpdateRequest extends UserUpdateRequest {
    @Builder
    public ManagerUpdateRequest(UUID id, String firstName, String lastName, String login, Boolean active) {
        super(id, firstName, lastName, login, active);
    }

    public Manager ToManager() {
        return new Manager(getId(), getFirstName(), getLastName(), getLogin(), getActive(), null);
    }
}
