package com.example.controllers.integration;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.data.model.ClientEnt;
import com.example.tks.adapter.data.repositories.ClientRepository;
import com.example.tks.adapter.rest.model.dto.real_estate.RealEstateRequest;
import com.example.tks.adapter.rest.model.dto.rent.RentCreateRequest;
import com.example.tks.core.domain.model.Client;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.example.tks.app.web.PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientControllerTests extends ControllerTests {
    private final static String BASE_URI = "http://localhost";
    private static RealEstateRequest realEstate1;


    @Autowired
    ClientRepository clientRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void createNewClient() {
        clientRepository.create(new ClientEnt(UUID.randomUUID(), true));
    }

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @BeforeAll
    public static void createExamples() {
        realEstate1 = RealEstateRequest
                .builder()
                .name("test")
                .address("test")
                .price(10.1)
                .area(5.5)
                .build();
    }

    @Test
    void testGetRentsShouldReturnCorrectAmountOfRents() {
        Client client = clientRepository.get().get(0).toClient();

        String clientId = client.getId().toString();

        String realEstateId = given()
                .contentType(ContentType.JSON)
                .body(realEstate1)
                .when()
                .post("/realestate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", realEstateId)
                .pathParam("active", true)
                .when()
                .get("/realestate/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(0));


        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .realEstateId(UUID.fromString(realEstateId))
                .clientId(UUID.fromString(clientId))
                .startDate(LocalDate.now())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(rentCreateRequest)
                .when()
                .post("/rent")
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", realEstateId)
                .when()
                .get("/realestate/{id}/rents")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(1));
    }
}
