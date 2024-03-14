package com.example.pasik.seeder;

import com.example.pasik.config.MongoClientConfiguration;
import com.example.pasik.managers.AdministratorManager;
import com.example.pasik.managers.ClientManager;
import com.example.pasik.managers.RealEstateManager;
import com.example.pasik.managers.RentManager;
import com.example.pasik.model.Administrator;
import com.example.pasik.model.Client;
import com.example.pasik.model.RealEstate;
import com.example.pasik.model.Rent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Profile("dev")
public class LaunchSeeder implements CommandLineRunner {
    private final RealEstateManager realEstateManager;
    private final RentManager rentManager;
    private final ClientManager clientManager;
    private final AdministratorManager administratorManager;

    public LaunchSeeder(final RealEstateManager realEstateManager, final RentManager rentManager, final ClientManager clientManager, AdministratorManager administratorManager) {
        this.realEstateManager = realEstateManager;
        this.rentManager = rentManager;
        this.clientManager = clientManager;
        this.administratorManager = administratorManager;
    }

    @Override
    public void run(String... args) {
        loadUserData();
    }

    private void loadUserData() {
        Logger logger = LoggerFactory.getLogger(MongoClientConfiguration.class);
        try {
            Client client = new Client(null, "Bob", "Tyson", "booooob", true, "test123");
            clientManager.create(client);
            client = new Client(null, "John", "Wick", "JW", true, "test123");
            clientManager.create(client);
            client = new Client(null, "Lara", "Croft", "LC", true, "test123");
            clientManager.create(client);
            client = new Client(null, "Carl", "Johnson", "CJ", true, "test123");
            clientManager.create(client);
            Client client2 = new Client(null, "Neo", "Matrix", "neooo", true, "test123");
            client2 = clientManager.create(client2);
            Client inactiveClient = new Client(null, "Fiona", "Green", "shrek", false, "test123");
            inactiveClient = clientManager.create(inactiveClient);

            Administrator administrator = new Administrator(null, "John", "Doe", "JD", true, "test123");
            administratorManager.create(administrator);
            RealEstate realEstate = new RealEstate(null, "Big House", "Twinkle Street", 21, 15);
            realEstateManager.create(realEstate);
            realEstate = new RealEstate(null, "Small House", "Test123", 21, 15);
            realEstateManager.create(realEstate);
            realEstate = new RealEstate(null, "House", "Grove Street", 21, 15);
            realEstateManager.create(realEstate);
            RealEstate realEstate2 = new RealEstate(null, "Villa", "JumpStreet 21", 1500, 1000);
            realEstate2 = realEstateManager.create(realEstate2);

            Rent rent = rentManager.create(client2.getId(), realEstate2.getId(), LocalDate.now());
            logger.info("Data successfully initialized!");
        } catch (Exception e) {
            logger.error("Data initialization problem occurred!");
            throw new RuntimeException(e.getMessage());
        }

    }
}
