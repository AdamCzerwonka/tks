package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.aggregates.RealEstateServiceAdapter;
import com.example.tks.adapter.rest.aggregates.RentServiceAdapter;
import com.example.tks.adapter.rest.model.RealEstate.RealEstateRequest;
import com.example.tks.adapter.rest.model.RealEstate.RealEstateResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/realestate")
public class RealEstateController {
    private final RealEstateServiceAdapter realEstateServicePort;
    private final RentServiceAdapter rentServiceAdapter;

    public RealEstateController(final RealEstateServiceAdapter realEstateServicePort, final RentServiceAdapter rentServiceAdapter) {
        this.realEstateServicePort = realEstateServicePort;
        this.rentServiceAdapter = rentServiceAdapter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        RealEstateResponse result = realEstateServicePort.getById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<RealEstateResponse>> get() {
        return ResponseEntity.ok(realEstateServicePort.get());
    }

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<Rent>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentServiceAdapter.getByRealEstateId(id, current));
    }

    @PostMapping
    public ResponseEntity<RealEstate> create(@Valid @RequestBody RealEstateRequest request) throws URISyntaxException {
        var result = realEstateServicePort.create(request);
        return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid RealEstateRequest request) {
        realEstate.setId(id);
        RealEstate result = realEstateManager.update(request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            realEstateServicePort.delete(id);
        } catch (RealEstateRentedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }
}
