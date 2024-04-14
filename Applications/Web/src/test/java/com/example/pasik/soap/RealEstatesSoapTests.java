package com.example.pasik.soap;

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
    void testGetAll() {
        UUID uuid = UUID.randomUUID();
        realEstateService.create(new RealEstate(uuid, "name", "description", 1000, 10));
        String query = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rs="http://www.example.com/tks/soap">
                    <soap:Header/>
                    <soap:Body>
                        <rs:getRealEstatesRequest>
                        </rs:getRealEstatesRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        query = String.format(query, uuid);
        System.out.println(query);

        given()
                .contentType("text/xml")
                .body(query)
                .when()
                .post("http://localhost:" + port + "/ws")
                .then()
                .assertThat()
                .statusCode(500);
    }

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        String query = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rs="http://www.example.com/tks/soap">
                    <soap:Header/>
                    <soap:Body>
                        <rs:createRealEstateRequest>
                            <realEstate>
                                <id>%s</id>
                                <name>name</name>
                                <description>description</description>
                                <price>1000</price>
                            </realEstate>
                        </rs:createRealEstateRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        query = String.format(query, id);

        given()
                .contentType("text/xml")
                .body(query)
                .when()
                .post("http://localhost:" + port + "/ws")
                .then()
                .assertThat()
                .statusCode(500);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        String query = """
                <?xml version="1.0" encoding="UTF-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:rs="http://www.example.com/tks/soap">
                    <soap:Header/>
                    <soap:Body>
                        <rs:deleteRealEstateRequest>
                            <id>%s</id>
                        </rs:createRealEstateRequest>
                    </soap:Body>
                </soap:Envelope>
                """;

        query = String.format(query, id);

        given()
                .contentType("text/xml")
                .body(query)
                .when()
                .post("http://localhost:" + port + "/ws")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
