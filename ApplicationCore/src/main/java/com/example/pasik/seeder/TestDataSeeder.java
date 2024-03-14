package com.example.pasik.seeder;

import com.example.pasik.managers.ClientManager;
import com.example.pasik.managers.RealEstateManager;
import com.example.pasik.managers.RentManager;
import com.example.pasik.model.Client;
import com.example.pasik.model.RealEstate;
import com.example.pasik.model.Rent;
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
public class TestDataSeeder {
    private final RealEstateManager realEstateManager;
    private final RentManager rentManager;
    private final ClientManager clientManager;

    private static final List<RealEstate> realEstates = new ArrayList<>();
    private static final List<Rent> rents = new ArrayList<>();
    private static final List<Client> clients = new ArrayList<>();

    public TestDataSeeder(final RealEstateManager realEstateManager, final RentManager rentManager, final ClientManager clientManager) {
        this.realEstateManager = realEstateManager;
        this.rentManager = rentManager;
        this.clientManager = clientManager;
    }

    @Bean
    public CommandLineRunner initTestData() {
        realEstates.clear();
        rents.clear();
        clients.clear();

        return args -> {
            Client testClient = new Client(null, "firstName", "lastName", "login", true, "test123");
            testClient = clientManager.create(testClient);
            Client testClient2 = new Client(null, "firstName2", "lastName2", "login2", true, "test123");
            testClient2 = clientManager.create(testClient2);
            Client inactiveClient = new Client(null, "firstNameInactive", "lastNameInactive", "loginInactive", false, "test123");
            inactiveClient = clientManager.create(inactiveClient);

            clients.add(testClient);
            clients.add(testClient2);
            clients.add(inactiveClient);

            RealEstate testRealEstate = new RealEstate(null,"name", "address", 15, 15);
            testRealEstate = realEstateManager.create(testRealEstate);
            RealEstate testRealEstate2 = new RealEstate(null,"name2", "address2", 21, 21);
            testRealEstate2 = realEstateManager.create(testRealEstate2);

            realEstates.add(testRealEstate);
            realEstates.add(testRealEstate2);

            Rent rent = rentManager.create(testClient.getId(), testRealEstate.getId(), LocalDate.now());

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
