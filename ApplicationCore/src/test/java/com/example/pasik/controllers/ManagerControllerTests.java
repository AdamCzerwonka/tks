package com.example.pasik.controllers;

import com.example.pasik.model.dto.Manager.ManagerCreateRequest;
import com.example.pasik.model.dto.Manager.ManagerUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ManagerControllerTests {
    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/manager";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }

    @Test
    public void testCreateShouldPassWhenAddingCorrectData() {
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    public void testCreateShouldFailWhenAddingIncorrectData() {
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("")
                .lastName("")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testCreateShouldFailWhenDuplicatingLogin() {
        ManagerCreateRequest managerCreateRequest1 = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        ManagerCreateRequest managerCreateRequest2 = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName2")
                .lastName("TestLastName2")
                .login("testLogin1")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest1)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest1)
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

        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
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
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
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
                .body("firstName", equalTo(managerCreateRequest.getFirstName()))
                .body("lastName", equalTo(managerCreateRequest.getLastName()))
                .body("login", equalTo(managerCreateRequest.getLogin()))
                .body("active", equalTo(managerCreateRequest.getActive()));
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
    public void testFindManagersByLoginShouldReturnCorrectAmountOfData() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login/many/test")
                .then()
                .assertThat()
                .statusCode(200)
                .body("size()", equalTo(0));

        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
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
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", managerCreateRequest.getLogin())
                .when()
                .get("/login/single/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("firstName", equalTo(managerCreateRequest.getFirstName()))
                .body("lastName", equalTo(managerCreateRequest.getLastName()))
                .body("login", equalTo(managerCreateRequest.getLogin()))
                .body("active", equalTo(managerCreateRequest.getActive()));
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
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        ManagerUpdateRequest managerUpdateRequest = ManagerUpdateRequest
                .builder()
                .id(UUID.fromString(id))
                .firstName("NewTestFirstName")
                .lastName("NewTestLastName")
                .login("NewTestLogin")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerUpdateRequest)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("firstName", equalTo(managerUpdateRequest.getFirstName()))
                .body("lastName", equalTo(managerUpdateRequest.getLastName()))
                .body("login", equalTo(managerCreateRequest.getLogin()))
                .body("active", equalTo(managerUpdateRequest.getActive()));
    }

    @Test
    public void testUpdateShouldFailWhenPassingIncorrectData() throws JSONException {
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("id");

        ManagerUpdateRequest managerUpdateRequest = ManagerUpdateRequest
                .builder()
                .id(UUID.fromString(id))
                .firstName("")
                .lastName("")
                .login("NewTestLogin")
                .active(false)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(managerUpdateRequest)
                .when()
                .put()
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    public void testActivateShouldActiveDeactivatedAccount() {
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(false)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
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
                .body("active", equalTo(!managerCreateRequest.getActive()));
    }

    @Test
    public void testDeactivateShouldDeactivateActivatedAccount() {
        ManagerCreateRequest managerCreateRequest = ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("testLogin1")
                .active(true)
                .build();

        String id = given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
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
                .body("active", equalTo(!managerCreateRequest.getActive()));
    }
}
