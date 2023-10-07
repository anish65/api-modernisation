//package com.zand.system.transactionprocessor.messaging.publisher;
//
//import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
//import com.zand.system.common.messaging.kafka.publisher.KafkaProducer;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.lang.reflect.Field;
//import java.util.concurrent.CompletableFuture;
//
//import static org.mockito.Mockito.*;
//
//class KafkaFundTxnFailureEventPublisherTest {
//
//    private static final String TOPIC_NAME = "fund-transfer-failure";
//
//    @Mock
//    private KafkaProducer<String, FundTransferRSMessage> kafkaProducer;
//
//    private KafkaFundTxnFailureEventPublisher kafkaFundTxnFailureEventPublisher;
//
//    @BeforeEach
//    void setUp() throws NoSuchFieldException, IllegalAccessException {
//        MockitoAnnotations.openMocks(this);
//        kafkaFundTxnFailureEventPublisher = new KafkaFundTxnFailureEventPublisher(kafkaProducer);
//        Field f1 = kafkaFundTxnFailureEventPublisher.getClass().getField("topicName");
//        f1.setAccessible(true);
//        f1.set(kafkaFundTxnFailureEventPublisher, TOPIC_NAME);
//    }
//
//    @Test
//    void publish_shouldSendEventToKafka_whenCalled() {
//        FundTransferRSMessage event = new FundTransferRSMessage();
//        event.setReferenceNo("12345");
//
//        when(kafkaProducer.sendAsync(TOPIC_NAME, event.getReferenceNo(), event)).thenReturn(CompletableFuture.completedFuture(null));
//
//        kafkaFundTxnFailureEventPublisher.publish(event);
//
//        verify(kafkaProducer, times(1)).sendAsync(TOPIC_NAME, event.getReferenceNo(), event);
//    }
//
//    @Test
//    void publish_shouldLogError_whenSendingEventToKafkaFails() {
//        FundTransferRSMessage event = new FundTransferRSMessage();
//        event.setReferenceNo("12345");
//
//        when(kafkaProducer.sendAsync(TOPIC_NAME, event.getReferenceNo(), event)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Failed to send message to Kafka")));
//
//        kafkaFundTxnFailureEventPublisher.publish(event);
//
//        verify(kafkaProducer, times(1)).sendAsync(TOPIC_NAME, event.getReferenceNo(), event);
//        verifyNoMoreInteractions(kafkaProducer);
//    }
//}
