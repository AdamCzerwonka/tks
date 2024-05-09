package com.example.controllers.integration;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.rest.model.dto.rent.RentCreateRequest;
import com.example.tks.app.web.seeder.TestDataSeeder;
import com.example.tks.core.domain.exceptions.AccountInactiveException;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.domain.exceptions.RealEstateRentedException;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.ClientService;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.example.tks.app.web.PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@ActiveProfiles("test")
public class RentControllerTests extends ControllerTests {
    @Autowired
    private ClientService clientService;
    @Autowired
    private RealEstateService realEstateService;
    @Autowired
    private RentService rentService;

    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/rent";

    private static final List<RealEstate> realEstates = new ArrayList<>();
    private static final List<Rent> rents = new ArrayList<>();
    private static final List<Client> clients = new ArrayList<>();

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() throws RealEstateRentedException, NotFoundException, AccountInactiveException {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;

        realEstates.clear();
        rents.clear();
        clients.clear();

        Client testClient = new Client(null, true);
        testClient = clientService.create(testClient);
        Client testClient2 = new Client(null, true);
        testClient2 = clientService.create(testClient2);
        Client inactiveClient = new Client(null, false);
        inactiveClient = clientService.create(inactiveClient);

        clients.add(testClient);
        clients.add(testClient2);
        clients.add(inactiveClient);

        RealEstate testRealEstate = new RealEstate(null, "name", "address", 15, 15);
        testRealEstate = realEstateService.create(testRealEstate);
        RealEstate testRealEstate2 = new RealEstate(null, "name2", "address2", 21, 21);
        testRealEstate2 = realEstateService.create(testRealEstate2);

        realEstates.add(testRealEstate);
        realEstates.add(testRealEstate2);

        Rent rent = rentService.create(testClient.getId(), testRealEstate.getId(), LocalDate.now());
        rents.add(rent);
    }

    @Test
    public void testGetShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .assertThat()
                .body("size()", is(1))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testGetByIdShouldFailWhenNotGivenIdOfExistingRent() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testGetByIdShouldPassWhenGivenIdOfExistingRent() {
        Rent rent = rents.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(rent.getId().toString()))
                .body("client", notNullValue())
                .body("realEstate", notNullValue())
                .body("startDate", equalTo(rent.getStartDate().toString()));
    }

    @Test
    public void testCreateShouldPassWhenAddingCorrectData() {
        Client testClient = clients.get(0);
        RealEstate testRealEstate = realEstates.get(1);

        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .clientId(testClient.getId())
                .realEstateId(testRealEstate.getId())
                .startDate(LocalDate.now())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(rentCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    public void testCreateShouldFailWhenTryingToRentAlreadyRentedRealEstate() {
        Client testClient = clients.get(0);
        RealEstate testRealEstate = realEstates.get(0);

        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .clientId(testClient.getId())
                .realEstateId(testRealEstate.getId())
                .startDate(LocalDate.now())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(rentCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testCreateShouldFailWhenTryingToRentWithInactiveClient() {
        Client testClient = clients.get(2);
        RealEstate testRealEstate = realEstates.get(1);

        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .clientId(testClient.getId())
                .realEstateId(testRealEstate.getId())
                .startDate(LocalDate.now())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(rentCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testCreateShouldFailWhenTryingToSetStartDateInThePast() {
        Client testClient = clients.get(0);
        RealEstate testRealEstate = realEstates.get(1);

        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .clientId(testClient.getId())
                .realEstateId(testRealEstate.getId())
                .startDate(LocalDate.now().minusDays(1))
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(rentCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testEndRentShouldPassWhenEndingRent() {
        Rent rent = rents.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .post("/{id}/end")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(rent.getId().toString()))
                .body("endDate", notNullValue());
    }

    @Test
    public void testEndRentShouldFailWhenEndingAlreadyEndedRent() {
        Rent rent = rents.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .post("/{id}/end")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(rent.getId().toString()))
                .body("endDate", notNullValue());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .post("/{id}/end")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testEndRentShouldFailWhenTryingToEndRentThatHasNotStarted() {
        Client testClient = clients.get(0);
        RealEstate testRealEstate = realEstates.get(1);

        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .clientId(testClient.getId())
                .realEstateId(testRealEstate.getId())
                .startDate(LocalDate.now().plusDays(1))
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(rentCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.fromString(id))
                .when()
                .post("/{id}/end")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testDeleteShouldDeleteActiveRent() {
        Rent rent = rents.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .delete("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteShouldFailWhenTryingToDeleteFinishedRent() {
        Rent rent = rents.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .post("/{id}/end")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .delete("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", rent.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(rent.getId().toString()));

    }
}
