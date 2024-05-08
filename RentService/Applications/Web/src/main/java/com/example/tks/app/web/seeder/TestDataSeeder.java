package com.example.tks.app.web.seeder;

import com.example.tks.core.domain.model.Client;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import com.example.tks.ports.infrastructure.ClientPort;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Profile("test")
@AllArgsConstructor
public class TestDataSeeder {
    private final RealEstateService realEstateService;
    private final RentService rentService;
    private final ClientPort clientService;

    private static final List<RealEstate> realEstates = new ArrayList<>();
    private static final List<Rent> rents = new ArrayList<>();
    private static final List<Client> clients = new ArrayList<>();

    @Bean
    public CommandLineRunner initTestData() {
        realEstates.clear();
        rents.clear();
        clients.clear();

        return args -> {
            Client testClient = new Client(null, true);
            testClient = clientService.create(testClient);
            Client testClient2 = new Client(null, true);
            testClient2 = clientService.create(testClient2);
            Client inactiveClient = new Client(null, false);
            inactiveClient = clientService.create(inactiveClient);

            clients.add(testClient);
            clients.add(testClient2);
            clients.add(inactiveClient);

            RealEstate testRealEstate = new RealEstate(null, "name", "address", 15, 15);
            testRealEstate = realEstateService.create(testRealEstate);
            RealEstate testRealEstate2 = new RealEstate(null, "name2", "address2", 21, 21);
            testRealEstate2 = realEstateService.create(testRealEstate2);

            realEstates.add(testRealEstate);
            realEstates.add(testRealEstate2);

            Rent rent = rentService.create(testClient.getId(), testRealEstate.getId(), LocalDate.now());

            rents.add(rent);
        };
    }

    public static List<RealEstate> getRealEstates() {
        return Collections.unmodifiableList(realEstates);
    }

    public static List<Rent> getRents() {
        return Collections.unmodifiableList(rents);
    }

    public static List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }
}
