package com.example.pasik.controllers;

import com.example.pasik.model.dto.Client.ClientCreateRequest;
import com.example.pasik.model.dto.Client.ClientUpdateRequest;
import com.example.pasik.model.dto.RealEstate.RealEstateRequest;
import com.example.pasik.model.dto.Rent.RentCreateRequest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientControllerTests {
    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/client";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }

    @Test
    public void testCreateShouldPassWhenAddingCorrectData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    public void testCreateShouldFailWhenAddingIncorrectData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("")
                .lastName("")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testCreateShouldFailWhenDuplicatingLogin() {
        ClientCreateRequest clientCreteRequest1 = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        ClientCreateRequest clientCreteRequest2 = ClientCreateRequest
                .builder()
                .firstName("TestFirstName2")
                .lastName("TestLastName2")
                .login("testLogin1")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest1)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest2)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testGetShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(0));

        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .assertThat()
                .body("size()", is(1))
                .statusCode(200);
    }

    @Test
    public void testGetByIdShouldReturnCorrectData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("firstName", equalTo(clientCreteRequest.getFirstName()))
                .body("lastName", equalTo(clientCreteRequest.getLastName()))
                .body("login", equalTo(clientCreteRequest.getLogin()))
                .body("active", equalTo(clientCreteRequest.getActive()));
    }

    @Test
    public void testGetByIdShouldFailWhenPassingRandomId() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testFindClientsByLoginShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login/many/test")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(0));

        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login/many/test")
                .then()
                .assertThat()
                .body("size()", is(1))
                .statusCode(200);
    }

    @Test
    public void testGetByLoginShouldReturnCorrectData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", clientCreteRequest.getLogin())
                .when()
                .get("/login/single/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("firstName", equalTo(clientCreteRequest.getFirstName()))
                .body("lastName", equalTo(clientCreteRequest.getLastName()))
                .body("login", equalTo(clientCreteRequest.getLogin()))
                .body("active", equalTo(clientCreteRequest.getActive()));
    }

    @Test
    public void testGetByLoginShouldFailWhenPassingRandomLogin() {
        given()
                .contentType(ContentType.JSON)
                .get("/login/single/abc123")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testUpdateShouldPassWhenPassingCorrectData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        ClientUpdateRequest clientUpdateRequest = ClientUpdateRequest
                .builder()
                .id(UUID.fromString(id))
                .firstName("NewTestFirstName")
                .lastName("NewTestLastName")
                .login("NewTestLogin")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientUpdateRequest)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("firstName", equalTo(clientUpdateRequest.getFirstName()))
                .body("lastName", equalTo(clientUpdateRequest.getLastName()))
                .body("login", equalTo(clientCreteRequest.getLogin()))
                .body("active", equalTo(clientUpdateRequest.getActive()));
    }

    @Test
    public void testUpdateShouldFailWhenPassingIncorrectData() throws JSONException {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        ClientUpdateRequest clientUpdateRequest = ClientUpdateRequest
                .builder()
                .id(UUID.fromString(id))
                .firstName("")
                .lastName("")
                .login("NewTestLogin")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(clientUpdateRequest)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testActivateShouldActiveDeactivatedAccount() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(false)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/activate/" + id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("active", equalTo(!clientCreteRequest.getActive()));
    }

    @Test
    public void testDeactivateShouldDeactivateActivatedAccount() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/deactivate/" + id)
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("active", equalTo(!clientCreteRequest.getActive()));
    }

    @Test
    public void testGetRentsShouldReturnCorrectAmountOfData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(clientCreteRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .pathParam("active", true)
                .when()
                .get("/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(0));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .pathParam("active", false)
                .when()
                .get("/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(0));

        RealEstateRequest realEstateRequest = RealEstateRequest
                .builder()
                .name("test")
                .address("test")
                .area(10)
                .price(15)
                .build();

        RequestSpecification realEstateSpec = new RequestSpecBuilder().setBaseUri(BASE_URI + "/realestate").build();
        String realEstateId = given()
                .contentType(ContentType.JSON)
                .spec(realEstateSpec)
                .body(realEstateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        RentCreateRequest rentCreateRequest = RentCreateRequest
                .builder()
                .clientId(UUID.fromString(id))
                .realEstateId(UUID.fromString(realEstateId))
                .startDate(LocalDate.now())
                .build();

        RequestSpecification rentSpec = new RequestSpecBuilder().setBaseUri(BASE_URI + "/rent").build();
        String rentId = given()
                .contentType(ContentType.JSON)
                .spec(rentSpec)
                .body(rentCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .pathParam("active", true)
                .when()
                .get("/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(1));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .pathParam("active", false)
                .when()
                .get("/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(0));

        given()
                .contentType(ContentType.JSON)
                .spec(rentSpec)
                .pathParam("id", rentId)
                .when()
                .post("{id}/end")
                .then()
                .assertThat()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .pathParam("active", false)
                .when()
                .get("/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(1));

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .pathParam("active", true)
                .when()
                .get("/{id}/rents?current={active}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", is(0));
    }
}
