package com.example.tks.adapters.messagedata.MqListener;

import com.example.tks.adapters.messagedata.dto.client.UserMessage;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class MqListener {
    private final ClientService clientService;

    @RabbitListener(queues = "user-create-queue")
    public void listenCreate(UserMessage message) {
        clientService.create(message.toClient());
    }

    @RabbitListener(queues = "user-active-queue")
    public void listenActive(UserMessage message) throws NotFoundException {
        clientService.setActiveStatus(message.id(), message.active());
    }
}
