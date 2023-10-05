package com.zand.system.transactionprocessor.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zand.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionprocessor.service.TxnProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FundTransferEventListener implements KafkaConsumer<String, FundTransferRQMessage> {

    private final TxnProcessingService txnProcessingService;

    @SneakyThrows
    @Override
    @KafkaListener(topics = "${kafka.consumer.topic.initiate-fund-transfer}",
            groupId = "${kafka.consumer.group-id.initiate-fund-transfer}",
            concurrency = "5")
    public void receive(@Payload FundTransferRQMessage message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("Received message with key: {}, message: {}, partition: {} and offset: {}", key,
                new ObjectMapper().writeValueAsString(message), partition, offset);
        txnProcessingService.process(message);
    }

}
