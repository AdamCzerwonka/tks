package com.example.pasik.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "mongo")
public class MongoConfig {
    private String connectionString;
    private String username;
    private String password;
    private String dbName;
    private boolean dbClean = false;
}
