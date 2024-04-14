package com.example.pasik.controllers;

import com.example.pasik.MongoDbContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ControllerTests {
    @Container
    static MongoDbContainer mongoDbContainer = new MongoDbContainer();

    @BeforeAll
    public static void setUp() {
        mongoDbContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mongoDbContainer.stop();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        int port = mongoDbContainer.getMappedPort(27017);
        registry.add("mongo.connection-string", () -> "mongodb://localhost:" + port);

    }
}
