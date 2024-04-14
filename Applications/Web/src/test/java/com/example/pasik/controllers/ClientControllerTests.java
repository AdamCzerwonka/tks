package com.example.pasik.controllers;

import com.example.tks.adapter.rest.model.dto.client.ClientCreateRequest;
import com.example.tks.adapter.rest.model.dto.client.ClientUpdateRequest;
import com.example.tks.adapter.rest.model.dto.real_estate.RealEstateRequest;
import com.example.tks.adapter.rest.model.dto.rent.RentCreateRequest;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.example.tks.app.web.PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClientControllerTests extends ControllerTests {
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
    void testCreateShouldPassWhenAddingCorrectData() {
        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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
    void testCreateShouldFailWhenAddingIncorrectData() {
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
    void testCreateShouldFailWhenDuplicatingLogin() {
        ClientCreateRequest clientCreteRequest1 = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(true)
                .build();

        ClientCreateRequest clientCreteRequest2 = ClientCreateRequest
                .builder()
                .firstName("TestFirstName2")
                .lastName("TestLastName2")
                .login("testLogin1")
                .password("testPassword2")
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
    void testGetShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(0));

        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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
    void testGetByIdShouldReturnCorrectData() {
        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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
    void testGetByIdShouldFailWhenPassingRandomId() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", UUID.randomUUID())
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    void testFindClientsByLoginShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login/many/test")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(0));

        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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
    void testGetByLoginShouldReturnCorrectData() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
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
    void testGetByLoginShouldFailWhenPassingRandomLogin() {
        given()
                .contentType(ContentType.JSON)
                .get("/login/single/abc123")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    void testUpdateShouldPassWhenPassingCorrectData() {
        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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

        String etag = given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .header("Etag").transform(s -> s.replace("\"", ""));

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
                .header("If-Match", etag)
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
    void testUpdateShouldFailWhenPassingIncorrectData() throws JSONException {
        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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

        String etag = given()
                .contentType(ContentType.JSON)
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .header("Etag").transform(s -> s.replace("\"", ""));

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
                .header("If-Match", etag)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    void testActivateShouldActiveDeactivatedAccount() {
        ClientCreateRequest clientCreteRequest = ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
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
    void testDeactivateShouldDeactivateActivatedAccount() {
        ClientCreateRequest clientCreteRequest = getClientCreateRequest();

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

    ClientCreateRequest getClientCreateRequest() {
        return ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(true)
                .build();
    }
}
