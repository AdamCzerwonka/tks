package com.example.pasik.controllers;

import com.example.pasik.MongoDbContainer;
import com.example.tks.adapter.rest.model.dto.administrator.AdministratorCreateRequest;
import com.example.tks.adapter.rest.model.dto.administrator.AdministratorUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.example.tks.app.web.PasikApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdministratorControllerTests extends ControllerTests {
    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/administrator";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }

    @Test
    void testCreateShouldPassWhenAddingCorrectData() {
        AdministratorCreateRequest administratorCreateRequest = AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void testCreateShouldFailWhenAddingIncorrectData() {
        AdministratorCreateRequest administratorCreateRequest = AdministratorCreateRequest
                .builder()
                .firstName("")
                .lastName("")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    void testCreateShouldFailWhenDuplicatingLogin() {
        AdministratorCreateRequest administratorCreateRequest1 = AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(true)
                .build();

        AdministratorCreateRequest administratorCreateRequest2 = AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName2")
                .lastName("TestLastName2")
                .login("testLogin1")
                .password("testPassword2")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest1)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest2)
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

        AdministratorCreateRequest administratorCreateRequest = getAdministratorCreateRequest();

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
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
        AdministratorCreateRequest administratorCreateRequest = AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
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
                .body("firstName", equalTo(administratorCreateRequest.getFirstName()))
                .body("lastName", equalTo(administratorCreateRequest.getLastName()))
                .body("login", equalTo(administratorCreateRequest.getLogin()))
                .body("active", equalTo(administratorCreateRequest.getActive()));
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
    void testFindAdministratorsByLoginShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login/many/test")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(0));

        AdministratorCreateRequest administratorCreateRequest = getAdministratorCreateRequest();

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
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
        AdministratorCreateRequest administratorCreateRequest = getAdministratorCreateRequest();

        String id = given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", administratorCreateRequest.getLogin())
                .when()
                .get("/login/single/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("firstName", equalTo(administratorCreateRequest.getFirstName()))
                .body("lastName", equalTo(administratorCreateRequest.getLastName()))
                .body("login", equalTo(administratorCreateRequest.getLogin()))
                .body("active", equalTo(administratorCreateRequest.getActive()));
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
    void testUpdateShouldFailWhenPassingIncorrectData() throws JSONException {
        AdministratorCreateRequest administratorCreateRequest = getAdministratorCreateRequest();
        String id = given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
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
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .header(HttpHeaders.ETAG)
                .transform(s -> s.replace("\"", ""));

        AdministratorUpdateRequest administratorUpdateRequest = AdministratorUpdateRequest
                .builder()
                .id(UUID.fromString(id))
                .firstName("")
                .lastName("")
                .login("NewTestLogin")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(administratorUpdateRequest)
                .header(HttpHeaders.IF_MATCH, etag)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    void testActivateShouldActiveDeactivatedAccount() {
        AdministratorCreateRequest administratorCreateRequest = AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(false)
                .build();
        String id = given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
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
                .body("active", equalTo(!administratorCreateRequest.getActive()));
    }

    @Test
    void testDeactivateShouldDeactivateActivatedAccount() {
        AdministratorCreateRequest administratorCreateRequest = getAdministratorCreateRequest();
        String id = given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
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
                .body("active", equalTo(!administratorCreateRequest.getActive()));
    }

    AdministratorCreateRequest getAdministratorCreateRequest() {
        return AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .password("testPassword1")
                .active(true)
                .build();
    }
}
