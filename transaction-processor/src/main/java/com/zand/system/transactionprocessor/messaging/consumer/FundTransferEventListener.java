package com.zand.system.transactionprocessor.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zand.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionprocessor.config.RateLimitConfig;
import com.zand.system.transactionprocessor.service.TxnProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FundTransferEventListener implements KafkaConsumer<String, TransactionRQMessage> {

    private final TxnProcessingService txnProcessingService;

    private final KafkaListenerEndpointRegistry registry;

    private final RateLimitConfig rateLimitConfig;

    @Value("${kafka.consumer.topic.initiate-fund-transfer}")
    private String topicName;

    @Value("${kafka.consumer.id.initiate-fund-transfer}")
    private String topicId;


    @SneakyThrows
    @Override
    @KafkaListener(id = "${kafka.consumer.id.initiate-fund-transfer}", topics = "${kafka.consumer.topic.initiate-fund-transfer}",
            groupId = "${kafka.consumer.group-id.initiate-fund-transfer}", properties = {
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG + "=60000",
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG + "=1",
            ConsumerConfig.FETCH_MAX_BYTES_CONFIG + "=1",
            ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG + "=1",
            ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG+ "=1"
    }, concurrency = "2")
    public void receive(@Payload TransactionRQMessage message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("Received message with key: {}, message: {}, partition: {} and offset: {}", key,
                new ObjectMapper().writeValueAsString(message), partition, offset);
        if(rateLimitConfig.resolveBucket(topicName).tryConsume(1)){
            txnProcessingService.process(message);
        } else {
            registry.getListenerContainer(topicId).pause();
            while(!rateLimitConfig.resolveBucket(topicName).tryConsume(1)) {
                //Wait for 5 sec and try again.
                Thread.sleep(5000);
            }
            registry.getListenerContainer(topicId).resume();
            txnProcessingService.process(message);
        }


    }

}
