package com.example.tks.app.web.seeder;

import com.example.tks.adapter.data.config.MongoClientConfiguration;
import com.example.tks.core.domain.model.Administrator;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.ClientService;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.example.tks.core.services.interfaces.AdministratorService;
import com.example.tks.core.domain.model.Client;

import java.time.LocalDate;

@Component
@Profile("dev")
@AllArgsConstructor
public class LaunchSeeder implements CommandLineRunner {
    private final RealEstateService realEstateService;
    private final RentService rentService;
    private final ClientService clientService;
    private final AdministratorService administratorService;

    @Override
    public void run(String... args) {
        loadUserData();
    }

    private void loadUserData() {
        Logger logger = LoggerFactory.getLogger(MongoClientConfiguration.class);
        try {
            Client client = new Client(null, "Bob", "Tyson", "booooob", true, "test123");
            clientService.create(client);
            client = new Client(null, "John", "Wick", "JW", true, "test123");
            clientService.create(client);
            client = new Client(null, "Lara", "Croft", "LC", true, "test123");
            clientService.create(client);
            client = new Client(null, "Carl", "Johnson", "CJ", true, "test123");
            clientService.create(client);
            Client client2 = new Client(null, "Neo", "Matrix", "neooo", true, "test123");
            client2 = clientService.create(client2);
            Client inactiveClient = new Client(null, "Fiona", "Green", "shrek", false, "test123");
            inactiveClient = clientService.create(inactiveClient);

            Administrator administrator = new Administrator(null, "John", "Doe", "JD", true, "test123");
            administratorService.create(administrator);
            RealEstate realEstate = new RealEstate(null, "Big House", "Twinkle Street", 21, 15);
            realEstateService.create(realEstate);
            realEstate = new RealEstate(null, "Small House", "Test123", 21, 15);
            realEstateService.create(realEstate);
            realEstate = new RealEstate(null, "House", "Grove Street", 21, 15);
            realEstateService.create(realEstate);
            RealEstate realEstate2 = new RealEstate(null, "Villa", "JumpStreet 21", 1500, 1000);
            realEstate2 = realEstateService.create(realEstate2);

            Rent rent = rentService.create(client2.getId(), realEstate2.getId(), LocalDate.now());
            logger.info("Data successfully initialized!");
        } catch (Exception e) {
            logger.error("Data initialization problem occurred!");
            throw new RuntimeException(e.getMessage());
        }

    }
}
