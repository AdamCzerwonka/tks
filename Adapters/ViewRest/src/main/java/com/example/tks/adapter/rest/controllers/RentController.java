package com.example.tks.adapter.rest.controllers;


import com.example.tks.adapter.rest.aggregates.RentServiceAdapter;
import com.example.tks.adapter.rest.aggregates.UserServiceAdapter;
import com.example.tks.adapter.rest.model.Rent.RentCreateRequest;
import com.example.tks.adapter.rest.model.Rent.RentResponse;
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
    private final RentServiceAdapter rentManager;
    private final JwtUtil jwtUtil;
    private final UserServiceAdapter userManager;

    private User getUserFromComplexToken(String complexToken) {
        String token = complexToken.replace("Bearer ", "");
        String login = jwtUtil.getUserLogin(token);
        return userManager.getByLogin(login);
    }

    @PostMapping
    public ResponseEntity<RentResponse> create(@RequestBody @Valid RentCreateRequest rentRequest) throws NotFoundException
            , URISyntaxException, RealEstateRentedException, AccountInactiveException {
        var result = rentManager.create(
                rentRequest.getClientId(),
                rentRequest.getRealEstateId(),
                rentRequest.getStartDate());

        return ResponseEntity.created(new URI("https://localhost/rent/" + result.getId())).body(RentResponse.fromRent(result));
    }

    @PostMapping("/me")
    public ResponseEntity<RentResponse> create(@RequestHeader("Authorization") String complexToken, @RequestBody @Valid RentForUserCreateRequest rentForUserRequest) throws NotFoundException
            , URISyntaxException, RealEstateRentedException, AccountInactiveException {
        User user = getUserFromComplexToken(complexToken);
        var result = rentManager.create(
                user.getId(),
                rentForUserRequest.getRealEstateId(),
                rentForUserRequest.getStartDate());

        return ResponseEntity.created(new URI("https://localhost/rent/" + result.getId())).body(RentResponse.fromRent(result));
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<?> endRent(@PathVariable UUID id) throws NotFoundException, RentEndedException, InvalidEndRentDateException {
        rentManager.endRent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RentResponse>> get() {
        return ResponseEntity.ok(rentManager.get().stream().map(RentResponse::fromRent).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) throws NotFoundException {
        RentResponse result = rentManager.getById(id);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        rentManager.delete(id);

        return ResponseEntity.ok().build();
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
