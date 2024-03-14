package com.example.pasik.controllers;

import com.example.pasik.model.dto.Client.ClientCreateRequest;
import com.example.pasik.model.dto.RealEstate.RealEstateRequest;
import com.example.pasik.model.dto.Rent.RentCreateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RealEstateControllerTests {
    private final static String BASE_URI = "http://localhost";
    private static RealEstateRequest realEstate1;
    private static RealEstateRequest realEstate2;


    @LocalServerPort
    private int port;

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

        realEstate2 = RealEstateRequest
                .builder()
                .name("test2")
                .address("test2")
                .price(100.8)
                .area(1000)
                .build();
    }

    @Test
    public void testCreateShouldSuccessWhenPassingCorrectData() {
        given()
                .contentType(ContentType.JSON)
                .body(realEstate1)
                .when()
                .post("/realestate")
                .then()
                .assertThat()
                .body("id", Matchers.notNullValue())
                .statusCode(201);
    }

    @Test
    public void testCreateShouldFailWhenPassingIncorrectData() {
        var incorrectRealEstate = RealEstateRequest.builder().name("").address("atest").price(-10).build();

        given()
                .contentType(ContentType.JSON)
                .body(incorrectRealEstate)
                .when()
                .post("/realestate")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetAllShouldReturnAllExistingRealEstates() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/realestate")
                .then()
                .assertThat()
                .body("size()", is(0))
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .body(realEstate1)
                .when()
                .post("/realestate");

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/realestate")
                .then()
                .assertThat()
                .body("size()", is(1))
                .statusCode(200);
    }

    @Test
    public void testGetByIdShouldReturnRealEstateWhenPassingCorrectId() {
        String realEstateId = given()
                .contentType(ContentType.JSON)
                .body(realEstate1)
                .when()
                .post("/realestate")
                .then()
                .extract()
                .path("id");


        given()
                .contentType(ContentType.JSON)
                .pathParam("id", realEstateId)
                .when()
                .get("/realestate/{id}")
                .then()
                .assertThat()
                .body("name", Matchers.equalTo(realEstate1.getName()))
                .statusCode(200);
    }

    @Test
    public void testGetByIdShouldFailWhenPassingNonExistingId() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .when()
                .get("/realestate/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteShouldSuccessWhenRealEstateDoesNotHaveOpenedRents() {
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
                .pathParam("id", realEstateId)
                .when()
                .delete("/realestate/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());

        given()
                .pathParam("id", realEstateId)
                .when()
                .get("/realestate/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testDeleteShouldFailWhenRealEstateHaveOpenedRent() {
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

        ClientCreateRequest clientRequest = ClientCreateRequest
                .builder()
                .firstName("test")
                .lastName("Test")
                .login("test")
                .active(true)
                .build();

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientRequest)
                .when()
                .post("/client")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .path("id");

        RentCreateRequest rentRequest = RentCreateRequest
                .builder()
                .clientId(UUID.fromString(clientId))
                .realEstateId(UUID.fromString(realEstateId))
                .startDate(LocalDate.now())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(rentRequest)
                .when()
                .post("/rent")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

        given()
                .pathParam("id", realEstateId)
                .when()
                .delete("/realestate/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testUpdateShouldSuccessWhenGivenCorrectData() {
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

        var realEstateUpdate = RealEstateRequest.builder().name("test123").address("test123")
                .price(6)
                .area(10)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(realEstateUpdate)
                .pathParam("id", realEstateId)
                .when()
                .put("/realestate/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("name", is(realEstateUpdate.getName()));
    }

    @Test
    public void testUpdateShouldFailWhenGivenInvalidData() {
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

        var realEstateUpdate = RealEstateRequest
                .builder()
                .address("")
                .price(0)
                .area(-100)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(realEstateUpdate)
                .pathParam("id", realEstateId)
                .when()
                .put("/realestate/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetRentsShouldReturnCorrectAmountOfRents() {
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

        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String clientId = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post("/client")
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

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
