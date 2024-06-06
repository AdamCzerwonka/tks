package com.example.tks.adapters.compensation.listeners;

import com.example.tks.adapters.compensation.model.dto.client.UserMessage;
import com.example.tks.core.domain.exceptions.NotFoundException;
import com.example.tks.core.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserCompensationListener {
    private final ClientService clientService;

    @RabbitListener(queues = "user-create-compensation-queue")
    public void listenCreateCompensation(UserMessage message) {
        clientService.delete(message.id());
    }

    @RabbitListener(queues = "user-active-compensation-queue")
    public void listenActiveCompensation(UserMessage message) throws NotFoundException {
        clientService.setActiveStatus(message.id(), !message.active());
    }
}
