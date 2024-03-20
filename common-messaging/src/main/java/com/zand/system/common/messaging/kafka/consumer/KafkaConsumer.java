package com.zand.system.common.messaging.kafka.consumer;

import com.zand.system.common.messaging.kafka.message.KafkaMessage;

public interface KafkaConsumer<K extends String, T extends KafkaMessage> {
    void receive(T message, K key, Integer partition, Long offset);
}
