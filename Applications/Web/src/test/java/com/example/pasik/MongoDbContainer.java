package com.example.pasik;

import org.testcontainers.containers.GenericContainer;

public class MongoDbContainer extends GenericContainer<MongoDbContainer> {
    public MongoDbContainer() {
        super("mongo:7");
        withExposedPorts(27017);
        withEnv("MONGO_INITDB_ROOT_USERNAME", "root");
        withEnv("MONGO_INITDB_ROOT_PASSWORD", "root");
    }
}
