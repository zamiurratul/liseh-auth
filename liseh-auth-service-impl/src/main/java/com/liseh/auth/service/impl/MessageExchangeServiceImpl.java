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

import java.util.concurrent.ExecutionException;

@Service
public class MessageExchangeServiceImpl implements MessageExchangeService {
    @Value("${liseh-auth-request-topic-async}")
    private String authRequestTopicAsync;

    @Value("${liseh-auth-request-topic-sync}")
    private String authRequestTopicSync;

    @Value("${liseh-auth-reply-topic-sync}")
    private String authReplyTopicSync;

    private final KafkaTemplate<String, GenericKafkaObject> kafkaTemplate;
    private final ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate;

    public MessageExchangeServiceImpl(KafkaTemplate<String, GenericKafkaObject> kafkaTemplate, ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    @Override
    public GenericKafkaObject sendAndReceiveMessage(GenericKafkaObject request) throws InterruptedException, ExecutionException {
        ProducerRecord<String, GenericKafkaObject> producerRecord = new ProducerRecord<>(authRequestTopicSync, request);
        producerRecord.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, authReplyTopicSync.getBytes()));

        RequestReplyFuture<String, GenericKafkaObject, GenericKafkaObject> requestReplyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
        ConsumerRecord<String, GenericKafkaObject> consumerRecord =  requestReplyFuture.get();
        return consumerRecord.value();
    }

    @Override
    public void sendMessage(GenericKafkaObject request) throws InterruptedException, ExecutionException {
        kafkaTemplate.send(authRequestTopicAsync, request);
    }
}
