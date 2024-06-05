package com.example.tks.core.services.model.dto.client;

import com.example.tks.core.domain.model.Client;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

public record UserMessage(@JsonProperty("id") UUID id,
                          @JsonProperty("active") boolean active)
        implements Serializable {
    public Client toClient() {
        return new Client(id, active);
    }
}