package com.example.tks.adapter.rest.model.dto.client;

import com.example.tks.core.domain.model.Client;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreateRequest  {
    UUID id;
    Boolean active;

    public Client ToClient() {
        return new Client(getId(), getActive());
    }
}
