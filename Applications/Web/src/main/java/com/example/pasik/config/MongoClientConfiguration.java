package com.example.pasik.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MongoClientConfiguration {
    @Bean
    public MongoClient getMongoClient(MongoConfig mongoConfig) {
        Logger logger = LoggerFactory.getLogger(MongoClientConfiguration.class);
        ConnectionString connectionString = new ConnectionString(mongoConfig.getConnectionString());
        MongoCredential credential = MongoCredential
                .createCredential(
                        mongoConfig.getUsername(),
                        "admin",
                        mongoConfig.getPassword().toCharArray()
                );

        CodecRegistry pojoCodecRegistry =
                CodecRegistries.fromProviders(PojoCodecProvider.
                        builder().
                        automatic(true).
                        conventions(List.of(Conventions.ANNOTATION_CONVENTION)).
                        build());

        MongoClientSettings settings = MongoClientSettings
                .builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry)
                )
                .build();

        var client = MongoClients.create(settings);

        if (mongoConfig.isDbClean()) {
            client.getDatabase(mongoConfig.getDbName()).drop();
            logger.info("dropping db");
        }

        MongoDatabase db = client.getDatabase(mongoConfig.getDbName());

        createDbCollectionWithSchema(db, "realEstates", """
                {
                    "$jsonSchema": {
                        "bsonType": "object",
                        "required": ["address", "name", "area", "price"],
                        "properties": {
                            "address": {
                                "bsonType": "string",
                                "minLength": 1
                            },
                            "name": {
                                "bsonType": "string",
                                "minLength": 1
                            },
                            "area": {
                                "bsonType": "double"
                            },
                            "price": {
                                "bsonType": "double"
                            }
                        }
                    }
                }
                """);

        createDbCollectionWithSchema(db, "rents", """
                {
                    "$jsonSchema": {
                        "bsonType": "object",
                        "required": [
                            "client",
                            "realEstate",
                            "startDate"
                        ],
                        "properties": {
                            "client": {
                                "bsonType": "object"
                            },
                            "realEstate": {
                                "bsonType": "object"
                            },
                            "startDate": {
                                "bsonType": "date"
                            },
                            "endDate": {
                                "bsonType": "date"
                            }
                        }
                    }
                }
                """);

        createDbCollectionWithSchema(db, "users", """
                {
                    "$jsonSchema": {
                        "bsonType": "object",
                        "required": [
                            "firstName",
                            "lastName",
                            "login",
                            "active"
                        ],
                        "properties": {
                            "firstName": {
                                "bsonType": "string"
                            },
                            "lastName": {
                                "bsonType": "string"
                            },
                            "login": {
                                "bsonType": "string"
                            },
                            "active": {
                                "bsonType": "bool"
                            }
                        }
                    }
                }
                """);

        return client;
    }

    @Bean
    public MongoDatabase getMongoDatabase(MongoClient client, MongoConfig mongoConfig) {
        return client.getDatabase(mongoConfig.getDbName());
    }

    private void createDbCollectionWithSchema(MongoDatabase db, String collectionName, String schema) {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse(schema)
        );

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        db.createCollection(collectionName, createCollectionOptions);
    }
}
