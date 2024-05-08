package com.example.tks.adapter.rest.controllers;

import com.example.tks.adapter.rest.model.dto.rent.RentCreateRequest;
import com.example.tks.adapter.rest.model.dto.rent.RentForUserCreateRequest;
import com.example.tks.adapter.rest.model.dto.rent.RentResponse;
import com.example.tks.adapter.rest.model.Error;
import com.example.tks.core.domain.exceptions.*;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.domain.model.User;
import com.example.tks.core.services.JwtService;
import com.example.tks.core.services.interfaces.RentService;
import com.example.tks.core.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rent")
public class RentController {
    private final RentService rentService;
    private final JwtService jwtService;
    private final UserService userService;

    private User getUserFromComplexToken(String complexToken) throws NotFoundException {
        String token = complexToken.replace("Bearer ", "");
        String login = jwtService.getUserLogin(token);
        return userService.getByLogin(login);
    }

    @PostMapping
    public ResponseEntity<RentResponse> create(@RequestBody @Valid RentCreateRequest rentRequest) throws NotFoundException
            , URISyntaxException, RealEstateRentedException, AccountInactiveException {
        var result = rentService.create(
                rentRequest.getClientId(),
                rentRequest.getRealEstateId(),
                rentRequest.getStartDate());

        return ResponseEntity.created(new URI("https://localhost/rent/" + result.getId())).body(RentResponse.fromRent(result));
    }

    @PostMapping("/me")
    public ResponseEntity<RentResponse> create(@RequestHeader("Authorization") String complexToken, @RequestBody @Valid RentForUserCreateRequest rentForUserRequest) throws NotFoundException
            , URISyntaxException, RealEstateRentedException, AccountInactiveException {
        User user = getUserFromComplexToken(complexToken);
        var result = rentService.create(
                user.getId(),
                rentForUserRequest.getRealEstateId(),
                rentForUserRequest.getStartDate());

        return ResponseEntity.created(new URI("https://localhost/rent/" + result.getId())).body(RentResponse.fromRent(result));
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<?> endRent(@PathVariable UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException {
        rentService.endRent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RentResponse>> get() {
        return ResponseEntity.ok(rentService.get().stream().map(RentResponse::fromRent).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        Rent result = rentService.getById(id);

        return ResponseEntity.ok(RentResponse.fromRent(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            rentService.delete(id);

            return ResponseEntity.ok("Deleted");
        } catch (RentEndedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RealEstateRentedException.class)
    public Error handleRealEstateRented(RealEstateRentedException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("realEstateRented", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccountInactiveException.class)
    public Error handleAccountInactive(AccountInactiveException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("accountInactive", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RentEndedException.class)
    public Error handleRentEnded(RentEndedException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("rentEnded", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidEndRentDateException.class)
    public Error handleInvalidEndRentDate(InvalidEndRentDateException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("invalidEndDate", ex.getMessage());
        return new Error(HttpStatus.BAD_REQUEST.value(), errors);
    }
}
