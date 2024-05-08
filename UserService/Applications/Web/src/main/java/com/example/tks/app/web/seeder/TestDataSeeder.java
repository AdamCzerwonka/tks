package com.example.tks.app.web.seeder;

import com.example.tks.core.domain.model.Client;
import com.example.tks.core.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile("test")
@AllArgsConstructor
public class TestDataSeeder {
    private final ClientService clientService;
    private static final List<Client> clients = new ArrayList<>();

    @Bean
    public CommandLineRunner initTestData() {
        clients.clear();

        return args -> {
            Client testClient = new Client(null, "firstName", "lastName", "login", true, "test123");
            testClient = clientService.create(testClient);
            Client testClient2 = new Client(null, "firstName2", "lastName2", "login2", true, "test123");
            testClient2 = clientService.create(testClient2);
            Client inactiveClient = new Client(null, "firstNameInactive", "lastNameInactive", "loginInactive", false, "test123");
            inactiveClient = clientService.create(inactiveClient);

            clients.add(testClient);
            clients.add(testClient2);
            clients.add(inactiveClient);
        };
    }

    public static List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }
}
