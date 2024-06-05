package com.example.tks.adapter.rest.model.dto.client;

import com.example.tks.core.domain.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponse  {
    UUID id;
    Boolean active;

    public static ClientResponse fromClient(Client client) {
        return new ClientResponse(client.getId(), client.getActive());
    }
}
