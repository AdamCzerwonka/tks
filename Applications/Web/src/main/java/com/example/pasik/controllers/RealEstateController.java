package com.example.pasik.controllers;

import com.example.pasik.exceptions.NotFoundException;
import com.example.pasik.exceptions.RealEstateRentedException;
import com.example.pasik.managers.RealEstateManager;
import com.example.pasik.managers.RentManager;
import com.example.pasik.model.RealEstate;
import com.example.pasik.model.Rent;
import com.example.pasik.model.dto.RealEstate.RealEstateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/realestate")
public class RealEstateController {
    private final RealEstateManager realEstateManager;
    private final RentManager rentManager;

    public RealEstateController(final RealEstateManager realEstateManager, final RentManager rentManager) {
        this.realEstateManager = realEstateManager;
        this.rentManager = rentManager;
    }

    @PostMapping
    public ResponseEntity<RealEstate> create(@Valid @RequestBody RealEstateRequest request) throws URISyntaxException {
        var result = realEstateManager.create(request.toRealEstate());
        return ResponseEntity.created(new URI("http://localhost:8080/realestate/" + result.getId())).body(result);
    }

    @GetMapping
    public ResponseEntity<List<RealEstate>> get() {
        return ResponseEntity.ok(realEstateManager.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        RealEstate result = realEstateManager.getById(id);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/rents")
    public ResponseEntity<List<Rent>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentManager.getByRealEstateID(id, current));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            realEstateManager.delete(id);
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
        RealEstate result = realEstateManager.update(realEstate);
        return ResponseEntity.ok(result);
    }
}
