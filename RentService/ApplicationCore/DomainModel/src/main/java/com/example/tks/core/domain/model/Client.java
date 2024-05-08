package com.example.tks.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Client {
    private UUID id;
    private Boolean active;
}
