package com.example.tks.app.web;

import com.example.tks.adapter.security.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({RsaKeyProperties.class})
@SpringBootApplication(scanBasePackages = {"com.example.tks.*"})
public class PasikApplication {
    public static void main(String[] args) {
        SpringApplication.run(PasikApplication.class, args);
    }

}
