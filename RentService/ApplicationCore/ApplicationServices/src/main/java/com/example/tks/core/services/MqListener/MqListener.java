package com.example.tks.core.services.MqListener;

import com.example.tks.core.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
@Slf4j
public class MqListener {
    private final ClientService clientService;

    @RabbitListener(queues = "user-queue")
    public void listen(Message message) {
        System.out.println("Message read from myQueue : " + message.toString());
        log.info("Received message and deserialized to 'CustomMessage': {}", message.toString());
    }
}
