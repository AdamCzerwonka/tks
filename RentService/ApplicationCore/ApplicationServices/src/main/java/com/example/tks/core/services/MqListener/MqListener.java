package com.example.tks.core.services.MqListener;

import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.interfaces.ClientService;
import com.example.tks.core.services.model.dto.client.UserMessage;
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

    @RabbitListener(queues = "user-activate-queue")
    public void listenActive(UserMessage message) throws NotFoundException {
        clientService.setActiveStatus(message.id(), message.active());
    }
}
