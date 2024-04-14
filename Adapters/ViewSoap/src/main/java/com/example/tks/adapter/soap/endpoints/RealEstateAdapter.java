package com.example.tks.adapter.soap.endpoints;

import com.example.tks.adapter.soap.mappers.CreateRealEstateRequestToRealEstate;
import com.example.tks.adapter.soap.mappers.RealEstateToRealEstateSoap;
import com.example.tks.adapter.soap.model.*;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
@RequiredArgsConstructor
public class RealEstateAdapter {
    private final RealEstateService realEstateService;
    private final RentService rentService;

    private final String NAMESPACE_URI = "http://www.example.com/tks/soap";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createRealEstateRequest")
    @ResponsePayload
    public CreateRealEstateResponse createRealEstate(@RequestPayload CreateRealEstateRequest request) {
        var result = realEstateService.create(CreateRealEstateRequestToRealEstate.fromCreateRealEstateRequest(request));
        return new CreateRealEstateResponse(RealEstateToRealEstateSoap.fromRealEstate(result));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRealEstateRequest")
    @ResponsePayload
    public RealEstateResponse getRealEstate(@RequestPayload GetRealEstateRequest request) throws NotFoundException {
        RealEstate result = realEstateService.getById(request.getId());

        return new RealEstateResponse(RealEstateToRealEstateSoap.fromRealEstate(result));
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRealEstatesRequest")
    @ResponsePayload
    public GetRealEstatesResponse getRealEstates(@RequestPayload GetRealEstatesRequest request) {
        GetRealEstatesResponse response = new GetRealEstatesResponse();
        response.setRealEstates(realEstateService.get().stream().map(RealEstateToRealEstateSoap::fromRealEstate).toList());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteRealEstateRequest")
    @ResponsePayload
    public DeleteRealEstateResponse deleteRealEstate(@RequestPayload DeleteRealEstateRequest request) throws RealEstateRentedException {
        realEstateService.delete(request.getId());
        return new DeleteRealEstateResponse();
    }


//    public ResponseEntity<?> delete(@PathVariable UUID id) {
//        try {
//            realEstateService.delete(id);
//        } catch (RealEstateRentedException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//
//        return ResponseEntity.notFound().build();
//    }
//
//    public ResponseEntity<RealEstateResponse> update(
//            @PathVariable UUID id,
//            @RequestBody @Valid RealEstateRequest request) {
//        RealEstate realEstate = request.toRealEstate();
//        realEstate.setId(id);
//        RealEstate result = realEstateService.update(realEstate);
//        return ResponseEntity.ok(RealEstateResponse.fromRealEstate(result));
//    }
}
