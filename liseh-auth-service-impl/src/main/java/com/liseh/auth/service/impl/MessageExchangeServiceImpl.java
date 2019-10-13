package com.liseh.auth.service.impl;

import com.liseh.GenericKafkaObject;
import com.liseh.auth.service.MessageExchangeService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@Service
public class MessageExchangeServiceImpl implements MessageExchangeService {
    @Value("${spring.kafka.request-topic-sync}")
    private String requestTopicSync;

    @Value("${spring.kafka.request-topic-async}")
    private String requestTopicAsync;

    @Value("${spring.kafka.reply-topic-sync}")
    private String replyTopicSync;

    @Value("${spring.kafka.reply-topic-async}")
    private String replyTopicAsync;

//    private final KafkaTemplate<String, GenericKafkaObject> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate;

//    public MessageExchangeServiceImpl(KafkaTemplate<String, GenericKafkaObject> kafkaTemplate, ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//        this.replyingKafkaTemplate = replyingKafkaTemplate;
//    }


    public MessageExchangeServiceImpl(ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @Override
    public GenericKafkaObject sendAndReceiveMessage(GenericKafkaObject request) throws InterruptedException, ExecutionException {
        ProducerRecord<String, GenericKafkaObject> producerRecord = new ProducerRecord<>(requestTopicSync, request);
        producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopicSync.getBytes()));

        RequestReplyFuture<String, GenericKafkaObject, GenericKafkaObject> requestReplyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        requestReplyFuture.addCallback(new ListenableFutureCallback<ConsumerRecord<String, GenericKafkaObject>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ConsumerRecord<String, GenericKafkaObject> result) {
                // get consumer record value
                GenericKafkaObject reply = result.value();
                System.out.println("Reply: " + reply.toString());
            }
        });
        ConsumerRecord<String, GenericKafkaObject> consumerRecord =  requestReplyFuture.get();
        GenericKafkaObject reply = consumerRecord.value();
        return reply;
    }

//    @Override
//    public void sendMessage(GenericKafkaObject request) throws InterruptedException, ExecutionException {
//        kafkaTemplate.send(requestTopicAsync, request);
//    }
}
