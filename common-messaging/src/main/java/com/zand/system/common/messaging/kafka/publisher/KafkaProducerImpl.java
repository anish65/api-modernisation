package com.zand.system.common.messaging.kafka.publisher;

import com.zand.system.common.messaging.kafka.message.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Component
public class KafkaProducerImpl<K extends String, V extends KafkaMessage> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message) {
        send(topicName, key, message, null);
    }

    @Override
    public void send(String topicName, K key, V message, Supplier<SendResult<K, V>> callback) {
        log.error("Sending message={} to topic={}", message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            if(callback != null) {
                kafkaResultFuture.completeAsync(callback);
            }
            kafkaTemplate.send(topicName, key, message);
        } catch (KafkaException e) {
            log.error("Error in kafka producer with key: {}, message: {} and exception: {}", key, message,
                    e.getMessage());
            throw e;
        }
    }

    @Override
    public CompletableFuture<SendResult<K, V>> sendAsync(String topicName, K key, V message) {
        return kafkaTemplate.send(topicName, key, message);
    }

}
