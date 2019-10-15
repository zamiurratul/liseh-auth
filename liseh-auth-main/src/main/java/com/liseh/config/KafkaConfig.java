package com.liseh.config;

import com.liseh.GenericKafkaObject;
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
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
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

    @Value("${liseh-auth-reply-topic-sync}")
    private String replyTopic;

    @Value("${spring.request-reply.timeout-ms}")
    private Long replyTimeout;

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

    @Bean
    public ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingKafkaTemplate(ProducerFactory<String, GenericKafkaObject> pf, KafkaMessageListenerContainer<String, GenericKafkaObject> container) {
        ReplyingKafkaTemplate<String, GenericKafkaObject, GenericKafkaObject> replyingTemplate = new ReplyingKafkaTemplate<>(pf, container);
        replyingTemplate.setReplyTimeout(replyTimeout);
        return replyingTemplate;
    }

    @Bean
    public ProducerFactory<String, GenericKafkaObject> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaMessageListenerContainer<String, GenericKafkaObject> kafkaMessageListenerContainer(ConsumerFactory<String, GenericKafkaObject> cf) {
        ContainerProperties props = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(cf, props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericKafkaObject>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, GenericKafkaObject> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(replyingKafkaTemplate(producerFactory(), kafkaMessageListenerContainer(consumerFactory())));
        return factory;
    }
}
