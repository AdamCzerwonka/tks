package com.example.controllers.unit;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.rest.model.dto.manager.ManagerCreateRequest;
import com.example.tks.core.domain.model.Manager;
import com.example.tks.core.services.interfaces.ManagerService;
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
public class ManagerControllerTests extends ControllerTests {
    @MockBean
    private ManagerService managerService;
    
    List<Manager> managers;
    
    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/manager";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }
    @BeforeEach
    public void beforeEach() {
        managers = new ArrayList<>();
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "manager1", true, "Password"));
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "manager2", true, "Password"));
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "manager3", true, "Password"));
        managers.add(new Manager(UUID.randomUUID(), "TestFirstName4", "TestLastName4", "mngr4", true, "Password"));
    }

    ManagerCreateRequest getManagerCreateRequest() {
        return ManagerCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("manager1")
                .password("Password")
                .active(true)
                .build();
    }

    @Test
    void testGetShouldReturnCorrectAmountOfData() {
        when(managerService.get()).thenReturn(managers);

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
        when(managerService.create(any())).thenReturn(managers.get(0));

        ManagerCreateRequest managerCreateRequest = getManagerCreateRequest();

        given()
                .contentType(ContentType.JSON)
                .body(managerCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    void testGetByIdShouldReturnCorrectData() throws Exception {
        when(managerService.getById(any())).thenReturn(managers.get(0));

        Manager manager = managers.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", manager.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(manager.getId().toString()))
                .body("firstName", equalTo(manager.getFirstName()))
                .body("lastName", equalTo(manager.getLastName()))
                .body("login", equalTo(manager.getLogin()))
                .body("active", equalTo(manager.getActive()));
    }

    @Test
    void testFindClientsByLoginShouldReturnCorrectAmountOfData() throws Exception {
        when(managerService.findAllByLogin(any())).thenReturn(managers);

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
        when(managerService.getByLogin(any())).thenReturn(managers.get(0));

        Manager manager = managers.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", manager.getId())
                .when()
                .get("/login/single/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(manager.getId().toString()))
                .body("firstName", equalTo(manager.getFirstName()))
                .body("lastName", equalTo(manager.getLastName()))
                .body("login", equalTo(manager.getLogin()))
                .body("active", equalTo(manager.getActive()));
    }

    @Test
    void testActivateShouldReturnOK() throws Exception {
        doNothing().when(managerService).setActiveStatus(any(), anyBoolean());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/activate/" + managers.get(0).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testDeactivateShouldReturnOK() throws Exception {
        doNothing().when(managerService).setActiveStatus(any(), anyBoolean());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/deactivate/" + managers.get(0).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
