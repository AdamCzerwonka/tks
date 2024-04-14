package com.example.tks.adapter.soap.endpoints;

import com.example.tks.adapter.soap.model.dto.RealEstate.RealEstateRequest;
import com.example.tks.adapter.soap.model.dto.RealEstate.RealEstateResponse;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.UUID;


@Endpoint
@RequiredArgsConstructor
public class RealEstateSoap {
    private final RealEstateService realEstateService;
    private final RentService rentService;

    private final String NAMESPACE_URI = "http://www.example.com/tks/soap";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createRealEstate")
    @ResponsePayload
    public ResponseEntity<RealEstateResponse> create(@Valid @RequestPayload RealEstateRequest request) {
        var result = realEstateService.create(request.toRealEstate());
        return ResponseEntity.status(HttpStatus.CREATED).body(RealEstateResponse.fromRealEstate(result));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRealEstate")
    @ResponsePayload
    public ResponseEntity<List<RealEstateResponse>> get() {
        return ResponseEntity.ok(realEstateService.get().stream().map(RealEstateResponse::fromRealEstate).toList());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRealEstateById")
    @ResponsePayload
    public ResponseEntity<RealEstateResponse> getById(@RequestPayload UUID id) throws NotFoundException {
        RealEstate result = realEstateService.getById(id);

        return ResponseEntity.ok(RealEstateResponse.fromRealEstate(result));
    }

    public ResponseEntity<List<Rent>> getRents(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean current) {
        return ResponseEntity.ok(rentService.getByRealEstateID(id, current));
    }

    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            realEstateService.delete(id);
        } catch (RealEstateRentedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<RealEstateResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid RealEstateRequest request) {
        RealEstate realEstate = request.toRealEstate();
        realEstate.setId(id);
        RealEstate result = realEstateService.update(realEstate);
        return ResponseEntity.ok(RealEstateResponse.fromRealEstate(result));
    }
}
