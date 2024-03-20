package com.zand.system.common.messaging.kafka.consumer.config;

import com.zand.system.common.messaging.kafka.message.KafkaMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
public class KafkaConsumerConfig<T extends KafkaMessage> {}
