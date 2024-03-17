package com.example.tks.app.web.controllers;

import com.example.tks.app.web.model.dto.RealEstate.RealEstateRequest;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/realestate")
@RequiredArgsConstructor
public class RealEstateController {
    private final RealEstateService realEstateService;
    private final RentService rentService;


    @PostMapping
    public ResponseEntity<RealEstate> create(@Valid @RequestBody RealEstateRequest request) throws URISyntaxException {
        var result = realEstateService.create(request.toRealEstate());
        return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(result);
    }

    @GetMapping
    public ResponseEntity<List<RealEstate>> get() {
        return ResponseEntity.ok(realEstateService.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        RealEstate result = realEstateService.getById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<Rent>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentService.getByRealEstateID(id, current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            realEstateService.delete(id);
        } catch (RealEstateRentedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id,
            @RequestBody @Valid RealEstateRequest request) {
        RealEstate realEstate = request.toRealEstate();
        realEstate.setId(id);
        RealEstate result = realEstateService.update(realEstate);
        return ResponseEntity.ok(result);
    }
}
