package com.example.soap;

import com.example.tks.app.web.PasikApplication;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.services.interfaces.RealEstateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(classes = PasikApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RealEstatesSoapTests {
    @LocalServerPort
    private int port;

    @Autowired
    RealEstateService realEstateService;

    @Test
    void testGetById() {
        UUID uuid = UUID.randomUUID();
        var realEstate = realEstateService.create(new RealEstate(uuid, "name", "description", 1000, 10));
        String query = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rs="http://www.example.com/tks/soap">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <rs:getRealEstateRequest>
                            <id>%s</id>
                        </rs:getRealEstateRequest>
                    </soapenv:Body>
                </soapenv:Envelope>
                """;

        query = String.format(query, realEstate.getId());
        System.out.println(query);

        given()
                .contentType("text/xml")
                .body(query)
                .when()
                .post("http://localhost:" + port + "/ws")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();

        String query = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rs="http://www.example.com/tks/soap">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <rs:createRealEstateRequest>
                            <name type="xsd:string">name</name>
                            <address>description</address>
                            <area>10</area>
                            <price>1000</price>
                        </rs:createRealEstateRequest>
                    </soapenv:Body>
                </soapenv:Envelope>
                """;

        query = String.format(query, id);

        given()
                .contentType("text/xml")
                .body(query)
                .when()
                .post("http://localhost:" + port + "/ws")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    void testDelete() {
        UUID uuid = UUID.randomUUID();
        var realEstate = realEstateService.create(new RealEstate(uuid, "name", "description", 1000, 10));
        String query = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rs="http://www.example.com/tks/soap">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <rs:deleteRealEstateRequest>
                            <id type="xsd:string">%s</id>
                        </rs:deleteRealEstateRequest>
                    </soapenv:Body>
                </soapenv:Envelope>
                """;

        query = String.format(query, realEstate.getId());

        given()
                .contentType("text/xml")
                .body(query)
                .when()
                .post("http://localhost:" + port + "/ws")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
