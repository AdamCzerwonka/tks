package com.example.tks.app.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.example.tks.*"})
public class PasikApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasikApplication.class, args);
    }

}
