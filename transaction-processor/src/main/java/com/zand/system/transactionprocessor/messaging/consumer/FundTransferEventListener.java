package com.zand.system.transactionprocessor.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zand.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionprocessor.messaging.KafkaManager;
import com.zand.system.transactionprocessor.service.TxnProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FundTransferEventListener implements KafkaConsumer<String, FundTransferRQMessage> {

    private final TxnProcessingService txnProcessingService;

    private final KafkaListenerEndpointRegistry registry;

    @SneakyThrows
    @Override
    @KafkaListener(id = "test", topics = "${kafka.consumer.topic.initiate-fund-transfer}",
            groupId = "${kafka.consumer.group-id.initiate-fund-transfer}", properties = {
            ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG + "=60000",
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG + "=1",
            ConsumerConfig.FETCH_MAX_BYTES_CONFIG + "=1",
            ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG + "=1",
            ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG+ "=1"
    })
    public void receive(@Payload FundTransferRQMessage message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("Received message with key: {}, message: {}, partition: {} and offset: {}", key,
                new ObjectMapper().writeValueAsString(message), partition, offset);
        txnProcessingService.process(message);
    }

}
