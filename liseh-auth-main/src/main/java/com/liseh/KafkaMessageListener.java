package com.liseh;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {
    @KafkaListener(topics = "${liseh-bll-request-topic-async}")
    public void listenAsync(GenericKafkaObject request) throws InterruptedException {
        System.out.println("Async Received: " + request.toString());
    }

    @KafkaListener(topics = "${liseh-bll-request-topic-sync}")
    @SendTo
    public GenericKafkaObject listenSync(GenericKafkaObject request) throws InterruptedException {
        System.out.println("Sync Received: " + request.toString());
        GenericKafkaObject response = new GenericKafkaObject();
        response.setContent(request.getContent() + "-processed by LISEH-AUTH");
        return response;
    }
}