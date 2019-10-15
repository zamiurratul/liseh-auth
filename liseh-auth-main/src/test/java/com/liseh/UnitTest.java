package com.liseh;

import com.liseh.auth.service.MessageExchangeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UnitTest {

    @Autowired
    private MessageExchangeService messageExchangeService;

    @Test
    public void sendAndReceiveKafkaMessage() {
        try {
            GenericKafkaObject request = new GenericKafkaObject();
            request.setContent("Req from LISEH_AUTH_SYNC_TOPIC");
            System.out.println("______________________________________________________________");
            System.out.println("______________________________________________________________");
            System.out.println("______________________________________________________________");
            System.out.println("______________________________________________________________");
            GenericKafkaObject response = messageExchangeService.sendAndReceiveMessage(request);
            System.out.println("Response: " + response.toString());
            System.out.println("Done");
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Test
    public void sendKafkaMessage() {
        try {
            for (int i = 0; i < 10; i++) {
                GenericKafkaObject request = new GenericKafkaObject();
                request.setContent("Req from LISEH_AUTH_ASYNC_TOPIC " + i);
                messageExchangeService.sendMessage(request);
            }
            System.out.println("Done");
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
