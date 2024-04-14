package com.example.tks.adapter.soap.model.dto.User;

import com.example.tks.core.domain.model.User;
import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://www.example.com/tks/soap")
public class UserResponse implements Serializable {

    @XmlElement
    private UUID id;

    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;

    @XmlElement
    private String login;

    @XmlElement
    private Boolean active;

    @XmlElement
    private String role;
}
