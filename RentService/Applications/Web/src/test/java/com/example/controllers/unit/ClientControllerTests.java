package com.example.controllers.unit;

import com.example.controllers.ControllerTests;
import com.example.tks.adapter.rest.model.dto.client.ClientCreateRequest;
import com.example.tks.core.domain.model.Client;
import com.example.tks.core.services.interfaces.ClientService;
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
public class ClientControllerTests extends ControllerTests {
    @MockBean
    private ClientService clientService;

    List<Client> clients;

    private final static String BASE_URI = "http://localhost";
    private final static String ENDPOINT = "/client";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void configureRestAssured() {
        RestAssured.baseURI = BASE_URI + ENDPOINT;
        RestAssured.port = port;
    }
    @BeforeEach
    public void beforeEach() {
        clients = new ArrayList<>();
        clients.add(new Client(UUID.randomUUID(), "TestFirstName1", "TestLastName1", "client1", true, "Password"));
        clients.add(new Client(UUID.randomUUID(), "TestFirstName2", "TestLastName2", "client2", true, "Password"));
        clients.add(new Client(UUID.randomUUID(), "TestFirstName3", "TestLastName3", "client3", true, "Password"));
        clients.add(new Client(UUID.randomUUID(), "TestFirstName4", "TestLastName4", "client4", true, "Password"));
    }

    ClientCreateRequest getClientCreateRequest() {
        return ClientCreateRequest
                .builder()
                .firstName("TestFirstName1")
                .lastName("TestLastName1")
                .login("client1")
                .password("Password")
                .active(true)
                .build();
    }

    @Test
    void testGetShouldReturnCorrectAmountOfData() {
        when(clientService.get()).thenReturn(clients);

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
        when(clientService.create(any())).thenReturn(clients.get(0));

        ClientCreateRequest clientCreateRequest = getClientCreateRequest();

        given()
                .contentType(ContentType.JSON)
                .body(clientCreateRequest)
                .when()
                .post()
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", notNullValue());
    }

    @Test
    void testGetByIdShouldReturnCorrectData() throws Exception {
        when(clientService.getById(any())).thenReturn(clients.get(0));

        Client client = clients.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", client.getId())
                .when()
                .get("/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(client.getId().toString()))
                .body("firstName", equalTo(client.getFirstName()))
                .body("lastName", equalTo(client.getLastName()))
                .body("login", equalTo(client.getLogin()))
                .body("active", equalTo(client.getActive()));
    }

    @Test
    void testFindClientsByLoginShouldReturnCorrectAmountOfData() throws Exception {
        when(clientService.findAllByLogin(any())).thenReturn(clients);

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
        when(clientService.getByLogin(any())).thenReturn(clients.get(0));

        Client client = clients.get(0);

        given()
                .contentType(ContentType.JSON)
                .pathParam("id", client.getId())
                .when()
                .get("/login/single/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(client.getId().toString()))
                .body("firstName", equalTo(client.getFirstName()))
                .body("lastName", equalTo(client.getLastName()))
                .body("login", equalTo(client.getLogin()))
                .body("active", equalTo(client.getActive()));
    }

    @Test
    void testActivateShouldReturnOK() throws Exception {
        doNothing().when(clientService).setActiveStatus(any(), anyBoolean());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/activate/" + clients.get(0).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testDeactivateShouldReturnOK() throws Exception {
        doNothing().when(clientService).setActiveStatus(any(), anyBoolean());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/deactivate/" + clients.get(0).getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
