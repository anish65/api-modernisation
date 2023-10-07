package com.zand.system.transactionprocessor.messaging.publisher;

import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.common.messaging.kafka.publisher.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

public interface FundTxnSuccessEventPublisher {
    void publish(TransactionRSMessage event);
}

@Slf4j
@RequiredArgsConstructor
@Component
class KafkaFundTxnSuccessEventPublisher implements FundTxnSuccessEventPublisher {

    @Value("${kafka.publisher.topic.fund-transfer-success}")
    private String topicName;
    private final KafkaProducer<String, TransactionRSMessage> kafkaProducer;

    @Override
    public void publish(final TransactionRSMessage event) {
        kafkaProducer.sendAsync(topicName, event.getReferenceNo(), event).whenComplete((metadata, exception) -> {
            if (exception != null) {
                log.error("Error while sending fund transfer failure message to kafka with receipt id: {}. Error: {}",
                        event.getReferenceNo(), exception.getMessage());
            } else {
                log.info("Successfully sent fund transfer failure message to kafka with receipt id: {}",
                        event.getReferenceNo());
            }
        });
    }
}