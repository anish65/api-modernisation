package com.zand.system.transactionprocessor.messaging.publisher;

import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.common.messaging.kafka.publisher.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class KafkaFundTxnFailureEventPublisherTest {

    private static final String TOPIC_NAME = "fund-transfer-failure";

    @Mock
    private KafkaProducer<String, TransactionRSMessage> kafkaProducer;

    private KafkaFundTxnFailureEventPublisher kafkaFundTxnFailureEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaFundTxnFailureEventPublisher = new KafkaFundTxnFailureEventPublisher(kafkaProducer);
        ReflectionTestUtils.setField(kafkaFundTxnFailureEventPublisher, "topicName", TOPIC_NAME);
    }

    @Test
    void publish_shouldSendEventToKafka_whenCalled() {
        TransactionRSMessage event = new TransactionRSMessage();
        event.setReferenceNo("12345");

        when(kafkaProducer.sendAsync(TOPIC_NAME, event.getReferenceNo(), event)).thenReturn(CompletableFuture.completedFuture(null));

        kafkaFundTxnFailureEventPublisher.publish(event);

        verify(kafkaProducer, times(1)).sendAsync(TOPIC_NAME, event.getReferenceNo(), event);
    }

    @Test
    void publish_shouldLogError_whenSendingEventToKafkaFails() {
        TransactionRSMessage event = new TransactionRSMessage();
        event.setReferenceNo("12345");

        when(kafkaProducer.sendAsync(TOPIC_NAME, event.getReferenceNo(), event)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Failed to send message to Kafka")));

        kafkaFundTxnFailureEventPublisher.publish(event);

        verify(kafkaProducer, times(1)).sendAsync(TOPIC_NAME, event.getReferenceNo(), event);
        verifyNoMoreInteractions(kafkaProducer);
    }
}
