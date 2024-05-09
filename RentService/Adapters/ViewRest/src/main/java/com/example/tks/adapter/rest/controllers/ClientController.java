package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.model.dto.rent.RentResponse;
import com.example.tks.core.services.interfaces.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {
    private final RentService rentService;

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<RentResponse>> getClientRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentService.getByClientId(id, current).stream().map(RentResponse::fromRent).toList());
    }

}
