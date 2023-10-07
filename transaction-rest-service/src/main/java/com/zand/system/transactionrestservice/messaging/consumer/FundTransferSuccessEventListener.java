package com.zand.system.transactionrestservice.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zand.system.common.messaging.kafka.consumer.KafkaConsumer;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.transactionrestservice.service.TransactionService;
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
public class FundTransferSuccessEventListener implements KafkaConsumer<String, TransactionRSMessage> {

    private final TransactionService transactionService;

    @SneakyThrows
    @Override
    @KafkaListener(topics = "${kafka.consumer.topic.fund-transfer-success}",
            groupId = "${kafka.consumer.group-id.fund-transfer-success}")
    public void receive(@Payload TransactionRSMessage message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        log.info("Received Success message with key: {}, message: {}, partition: {} and offset: {}", key,
                new ObjectMapper().writeValueAsString(message), partition, offset);
        transactionService.updateTransactionDetails(message);
    }

}
