package com.example.controllers.unit;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.rest.model.dto.real_estate.RealEstateRequest;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.example.tks.app.web.PasikApplication.class)
@ExtendWith(MockitoExtension.class)
public class RealEstateControllerTests extends ControllerTests {
    @MockBean
    private RealEstateService realEstateService;
    @MockBean
    private RentService rentService;

    List<RealEstate> realEstates;
    List<Rent> rents;

    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/realestate";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }

    @BeforeEach
    public void beforeEach() {
        realEstates = new ArrayList<>();
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName1", "Random address 21/3", 12.5, 2000));
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName2", "Random address 21/4", 15.5, 3000));
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName3", "Random address 21/5", 20.5, 4000));
        realEstates.add(new RealEstate(UUID.randomUUID(), "TestName4", "Random address 21/6", 25.5, 5000));
        rents = new ArrayList<>();
        rents.add(new Rent(UUID.randomUUID(),
                new Client(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "client1", true, "Password"),
                realEstates.get(0),
                LocalDate.now(),
                null));
    }

    RealEstateRequest getRealEstateRequest() {
        return RealEstateRequest
                .builder()
                .name("TestName1")
                .address("Random address 21/34")
                .area(13.4)
                .price(5454)
                .build();
    }

    @Test
    void testGetShouldReturnCorrectAmountOfData() {
        when(realEstateService.get()).thenReturn(realEstates);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(4));
    }

    @Test
    void testGetByIdShouldReturnCorrectData() throws Exception {
        RealEstate realEstate = realEstates.get(0);
        when(realEstateService.getById(any())).thenReturn(realEstate);
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", realEstate.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(realEstate.getId().toString()))
                .body("name", equalTo(realEstate.getName()))
                .body("address", equalTo(realEstate.getAddress()));
    }

    @Test
    void testCreateShouldPassWhenAddingCorrectData() throws Exception {
        when(realEstateService.create(any())).thenReturn(realEstates.get(0));

        RealEstateRequest realEstateRequest = getRealEstateRequest();

        given()
                .contentType(ContentType.JSON)
                .body(realEstateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    void testGetRentsShouldReturnCorrectAmountOfData() throws Exception {
        when(rentService.getByRealEstateID(any(), anyBoolean())).thenReturn(rents);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", realEstates.get(0).getId())
                .when()
                .get("/{id}/rents")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }


}
