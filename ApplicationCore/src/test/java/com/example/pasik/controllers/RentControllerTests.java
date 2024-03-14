package com.example.pasik.controllers;

import com.example.pasik.model.Client;
import com.example.pasik.model.RealEstate;
import com.example.pasik.model.Rent;
import com.example.pasik.model.dto.Rent.RentCreateRequest;
import com.example.pasik.seeder.TestDataSeeder;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RentControllerTests {
    private final static String BASE_URI = "http://localhost/rent";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
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
        List<Rent> testRents = TestDataSeeder.getRents();
        Rent rent = testRents.get(0);

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
        List<Client> testClients = TestDataSeeder.getClients();
        List<RealEstate> testRealEstates = TestDataSeeder.getRealEstates();
        Client testClient = testClients.get(0);
        RealEstate testRealEstate = testRealEstates.get(1);

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
        List<Client> testClients = TestDataSeeder.getClients();
        List<RealEstate> testRealEstates = TestDataSeeder.getRealEstates();
        Client testClient = testClients.get(0);
        RealEstate testRealEstate = testRealEstates.get(0);

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
        List<Client> testClients = TestDataSeeder.getClients();
        List<RealEstate> testRealEstates = TestDataSeeder.getRealEstates();
        Client testClient = testClients.get(2);
        RealEstate testRealEstate = testRealEstates.get(1);

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
        List<Client> testClients = TestDataSeeder.getClients();
        List<RealEstate> testRealEstates = TestDataSeeder.getRealEstates();
        Client testClient = testClients.get(0);
        RealEstate testRealEstate = testRealEstates.get(1);

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
        List<Rent> testRents = TestDataSeeder.getRents();
        Rent rent = testRents.get(0);

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
        List<Rent> testRents = TestDataSeeder.getRents();
        Rent rent = testRents.get(0);

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
        List<Client> testClients = TestDataSeeder.getClients();
        List<RealEstate> testRealEstates = TestDataSeeder.getRealEstates();
        Client testClient = testClients.get(0);
        RealEstate testRealEstate = testRealEstates.get(1);

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
        List<Rent> testRents = TestDataSeeder.getRents();
        Rent rent = testRents.get(0);

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
        List<Rent> testRents = TestDataSeeder.getRents();
        Rent rent = testRents.get(0);

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
