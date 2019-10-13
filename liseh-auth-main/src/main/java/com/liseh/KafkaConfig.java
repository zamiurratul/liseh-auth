package com.liseh;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Value("${spring.kafka.request-topic-sync}")
    private String requestTopicSync;

    @Value("${spring.kafka.reply-topic-sync}")
    private String replyTopicSync;

    @Value("${spring.kafka.reply-topic-async}")
    private String requestTopicAsync;

    @Value("${spring.kafka.reply-topic-async}")
    private String replyTopicAsync;

    @Value("${spring.request-reply.timeout-ms}")
    private Long replyTimeout;

    @Bean
    public ProducerFactory<String, GenericKafkaObject> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public ConsumerFactory<String, GenericKafkaObject> consumerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        return new DefaultKafkaConsumerFactory<>(configs,
                new StringDeserializer(),
                new JsonDeserializer<>(GenericKafkaObject.class));
    }

//    @Bean
//    public KafkaTemplate<String, GenericKafkaObject> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }

    @Bean
    public ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate() {
        ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingTemplate = new ReplyingKafkaTemplate<>(producerFactory(), kafkaMessageListenerContainer());
        replyingTemplate.setReplyTimeout(replyTimeout);
        return replyingTemplate;
    }

    @Bean
    public KafkaMessageListenerContainer<String, GenericKafkaObject> kafkaMessageListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties(replyTopicSync, replyTopicAsync);
        return new KafkaMessageListenerContainer<>(consumerFactory(), containerProperties);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericKafkaObject>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GenericKafkaObject> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setReplyTemplate(kafkaTemplate());
        factory.setReplyTemplate(replyingKafkaTemplate());
        return factory;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic requestTopicSyncKafka() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", replyTimeout.toString());
        return new NewTopic(requestTopicSync, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyTopicSyncKafka() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", replyTimeout.toString());
        return new NewTopic(replyTopicSync, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic requestTopicAsyncKafka() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", replyTimeout.toString());
        return new NewTopic(requestTopicAsync, 2, (short) 2).configs(configs);
    }

    @Bean
    public NewTopic replyTopicAsyncKafka() {
        Map<String, String> configs = new HashMap<>();
        configs.put("retention.ms", replyTimeout.toString());
        return new NewTopic(replyTopicAsync, 2, (short) 2).configs(configs);
    }
}