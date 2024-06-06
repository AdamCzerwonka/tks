package com.example.tks.adapters.messagedata.MqListener;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.example.tks.adapters.messagedata.dto.client.UserMessage;
import com.example.tks.core.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class MqListener {
    private final ClientService clientService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "user-create-queue")
    public void listenCreate(UserMessage message) {
        try {
        clientService.create(message.toClient());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend("appExchange", "messages.create.compensation.key", message);
        }
    }

    @RabbitListener(queues = "user-active-queue")
    public void listenActive(UserMessage message) {
        try {
        clientService.setActiveStatus(message.id(), message.active());
        } catch (Exception e) {
            rabbitTemplate.convertAndSend("appExchange", "messages.active.compensation.key", message);
        }
    }
}
