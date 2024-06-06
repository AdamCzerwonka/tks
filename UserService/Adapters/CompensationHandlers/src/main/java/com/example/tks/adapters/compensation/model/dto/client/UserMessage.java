package com.example.tks.adapters.compensation.model.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

public record UserMessage(@JsonProperty("id") UUID id,
                          @JsonProperty("active") boolean active)
        implements Serializable {
}