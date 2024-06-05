package com.example.tks.adapter.rest.model.dto.client;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserMessage(@JsonProperty("id") UUID id,
                            @JsonProperty("active") boolean active)
        implements Serializable {
}