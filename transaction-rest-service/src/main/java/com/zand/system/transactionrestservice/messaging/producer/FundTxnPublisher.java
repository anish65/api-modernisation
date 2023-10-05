package com.zand.system.transactionrestservice.messaging.producer;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.publisher.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

public interface FundTxnPublisher {
    void publish(FundTransferRQMessage event);

    CompletableFuture publishAsync(FundTransferRQMessage event);
}

@Slf4j
@RequiredArgsConstructor
@Component
class KafkaFundTnxPublisher implements FundTxnPublisher {

    @Value("${kafka.publisher.topic.initiate-fund-transfer}")
    private String topicName;
    private final KafkaProducer<String, FundTransferRQMessage> kafkaProducer;

    @Override
    public void publish(final FundTransferRQMessage event) {
        String fundTransferId = String.valueOf(RandomUtil.getPositiveInt());
        try {
            kafkaProducer.send(topicName, fundTransferId, event);
        } catch (Exception e) {
            log.error("Error while sending FundTransfer message to kafka with payment id: {}. Error: {}",
                    fundTransferId, e.getMessage());
        }
    }

    @Override
    public CompletableFuture<SendResult<String, FundTransferRQMessage>> publishAsync(FundTransferRQMessage event) {
        return kafkaProducer.sendAsync(topicName, event.getReferenceNo(), event);
    }
}