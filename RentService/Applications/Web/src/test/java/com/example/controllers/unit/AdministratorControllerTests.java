package com.example.controllers.unit;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.rest.model.dto.administrator.AdministratorCreateRequest;
import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.services.interfaces.AdministratorService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.example.tks.app.web.PasikApplication.class)
@ExtendWith(MockitoExtension.class)
public class AdministratorControllerTests extends ControllerTests {
    @MockBean
    private AdministratorService administratorService;

    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/administrator";

    List<Administrator> admins;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }

    @BeforeEach
    public void beforeEach() {
        admins = new ArrayList<>();
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "admin1", true, "Password"));
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "admin2", true, "Password"));
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "admin3", true, "Password"));
        admins.add(new Administrator(UUID.randomUUID(), "TestFirstName4", "TestLastName4", "ad4", true, "Password"));
    }

    AdministratorCreateRequest getAdministratorCreateRequest() {
        return AdministratorCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("admin1")
                .password("Password")
                .active(true)
                .build();
    }

    @Test
    void testGetShouldReturnCorrectAmountOfData() {
        when(administratorService.get()).thenReturn(admins);

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
    void testCreateShouldPassWhenAddingCorrectData() throws Exception {
        when(administratorService.create(any())).thenReturn(admins.get(0));

        AdministratorCreateRequest administratorCreateRequest = getAdministratorCreateRequest();

        given()
                .contentType(ContentType.JSON)
                .body(administratorCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    void testGetByIdShouldReturnCorrectData() throws Exception {
        when(administratorService.getById(any())).thenReturn(admins.get(0));

        Administrator administrator = admins.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", administrator.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(administrator.getId().toString()))
                .body("firstName", equalTo(administrator.getFirstName()))
                .body("lastName", equalTo(administrator.getLastName()))
                .body("login", equalTo(administrator.getLogin()))
                .body("active", equalTo(administrator.getActive()));
    }

    @Test
    void testFindClientsByLoginShouldReturnCorrectAmountOfData() throws Exception {
        when(administratorService.findAllByLogin(any())).thenReturn(admins);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/login/many/test")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(4));
    }

    @Test
    void testGetByLoginShouldReturnCorrectData() throws Exception {
        when(administratorService.getByLogin(any())).thenReturn(admins.get(0));

        Administrator administrator = admins.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", administrator.getId())
                .when()
                .get("/login/single/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(administrator.getId().toString()))
                .body("firstName", equalTo(administrator.getFirstName()))
                .body("lastName", equalTo(administrator.getLastName()))
                .body("login", equalTo(administrator.getLogin()))
                .body("active", equalTo(administrator.getActive()));
    }

    @Test
    void testActivateShouldReturnOK() throws Exception {
        doNothing().when(administratorService).setActiveStatus(any(), anyBoolean());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/activate/" + admins.get(0).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testDeactivateShouldReturnOK() throws Exception {
        doNothing().when(administratorService).setActiveStatus(any(), anyBoolean());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/deactivate/" + admins.get(0).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
