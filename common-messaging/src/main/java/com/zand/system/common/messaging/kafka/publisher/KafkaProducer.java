package com.zand.system.common.messaging.kafka.publisher;

import com.zand.system.common.messaging.kafka.message.KafkaMessage;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface KafkaProducer<K extends String, V extends KafkaMessage> {
    void send(String topicName, K key, V message);

    void send(String topicName, K key, V message, Supplier<SendResult<K, V>> callback);

    CompletableFuture<SendResult<K, V>> sendAsync(String topicName, K key, V message);
}

