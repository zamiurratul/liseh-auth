package com.liseh.kafka;

import com.liseh.GenericKafkaObject;
import com.liseh.auth.constant.EventType;
import com.liseh.auth.service.UserEventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    private final UserEventService userEventService;

    public MessageListener(UserEventService userEventService) {
        this.userEventService = userEventService;
    }

    @KafkaListener(topics = "${liseh-bll-request-topic-async}", autoStartup = "${kafka.listen.auto.start}")
    public void listenAsync(GenericKafkaObject request) throws InterruptedException {
        switch (request.getEventType()) {
            case EventType.USER:
                userEventService.onUserEvent(request);
                break;
            default:
                break;
        }

    }

    @KafkaListener(topics = "${liseh-bll-request-topic-sync}", autoStartup = "${kafka.listen.auto.start}")
    @SendTo
    public GenericKafkaObject listenSync(GenericKafkaObject request) throws InterruptedException {
        System.out.println("Sync Received: " + request.toString());
        GenericKafkaObject response = new GenericKafkaObject();
        response.setContent(request.getContent() + "-processed by LISEH-AUTH");
        return response;
    }
}