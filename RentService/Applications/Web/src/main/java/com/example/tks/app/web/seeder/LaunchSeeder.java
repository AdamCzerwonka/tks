package com.example.tks.app.web.seeder;

import com.example.tks.adapter.data.config.MongoClientConfiguration;
import com.example.tks.core.domain.model.RealEstate;
import com.example.tks.core.domain.model.Rent;
import com.example.tks.core.services.interfaces.RealEstateService;
import com.example.tks.core.services.interfaces.RentService;
import com.example.tks.ports.infrastructure.ClientPort;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.example.tks.core.domain.model.Client;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Profile("dev")
@AllArgsConstructor
public class LaunchSeeder implements CommandLineRunner {
    private final RealEstateService realEstateService;
    private final RentService rentService;
    private final ClientPort clientService;

    @Override
    public void run(String... args) {
        loadUserData();
    }

    private void loadUserData() {
        Logger logger = LoggerFactory.getLogger(MongoClientConfiguration.class);
        try {
//            Client client = new Client(UUID.randomUUID(), true);
//            clientService.create(client);
//            client = new Client(UUID.randomUUID(), true);
//            clientService.create(client);
//            client = new Client(UUID.randomUUID(), true);
//            clientService.create(client);
//            client = new Client(UUID.randomUUID(), true);
//            clientService.create(client);
//            Client client2 = new Client(UUID.randomUUID(), true);
//            client2 = clientService.create(client2);
//            Client inactiveClient = new Client(UUID.randomUUID(), false);
//            clientService.create(inactiveClient);
//
//            RealEstate realEstate = new RealEstate(null, "Big House", "Twinkle Street", 21, 15);
//            realEstateService.create(realEstate);
//            realEstate = new RealEstate(null, "Small House", "Test123", 21, 15);
//            realEstateService.create(realEstate);
//            realEstate = new RealEstate(null, "House", "Grove Street", 21, 15);
//            realEstateService.create(realEstate);
//            RealEstate realEstate2 = new RealEstate(null, "Villa", "JumpStreet 21", 1500, 1000);
//            realEstate2 = realEstateService.create(realEstate2);
//
//            rentService.create(client2.getId(), realEstate2.getId(), LocalDate.now());
//            logger.info("Data successfully initialized!");
        } catch (Exception e) {
            logger.error("Data initialization problem occurred!");
            throw new RuntimeException(e.getMessage());
        }

    }
}
