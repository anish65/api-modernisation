package com.zand.system.transactionrestservice.messaging.producer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.publisher.KafkaProducer;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class FundTnxPublisherTest {

    @Test
    public void testPublish() throws IllegalAccessException {
        // Create a mock FundTransferRQMessage object
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("123456");

        // Create a mock KafkaProducer object
        KafkaProducer<String, TransactionRQMessage> kafkaProducer = mock(KafkaProducer.class);

        // Create a new KafkaFundTnxPublisher object
        KafkaFundTnxPublisher kafkaFundTnxPublisher = new KafkaFundTnxPublisher(kafkaProducer);

        // Call the publish method and verify that the kafkaProducer.send method was called with the expected message
        String topic = "initiate-fund-transfer";
        FieldUtils.writeField(kafkaFundTnxPublisher, "topicName", topic, true);
        kafkaFundTnxPublisher.publish(message);
        verify(kafkaProducer).send(anyString(), anyString(), any());
    }

    @Test
    public void testPublishAsync() throws ExecutionException, InterruptedException, IllegalAccessException {
        // Create a mock FundTransferRQMessage object
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("123456");

        // Create a mock KafkaProducer object
        KafkaProducer<String, TransactionRQMessage> kafkaProducer = mock(KafkaProducer.class);

        // Create a new KafkaFundTnxPublisher object
        KafkaFundTnxPublisher kafkaFundTnxPublisher = new KafkaFundTnxPublisher(kafkaProducer);
        FieldUtils.writeField(kafkaFundTnxPublisher, "topicName", "initiate-fund-transfer", true);

        // Create a mock SendResult object
        SendResult<String, TransactionRQMessage> sendResult = mock(SendResult.class);
        ProducerRecord<String, TransactionRQMessage> producerRecord = new ProducerRecord<>("initiate-fund-transfer", "123456", message);
        RecordMetadata recordMetadata = new RecordMetadata(null, 0, 0, 0L, 0L, 0, 0);
        when(sendResult.getProducerRecord()).thenReturn(producerRecord);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);

        // Create a mock CompletableFuture object
        CompletableFuture<SendResult<String, TransactionRQMessage>> completableFuture = new CompletableFuture<>();
        completableFuture.complete(sendResult);

        // Set up the kafkaProducer.sendAsync method to return the mock CompletableFuture object
        when(kafkaProducer.sendAsync("initiate-fund-transfer", "123456", message)).thenReturn(completableFuture);

        // Call the publishAsync method and verify that the kafkaProducer.sendAsync method was called with the expected message
        CompletableFuture<SendResult<String, TransactionRQMessage>> result = kafkaFundTnxPublisher.publishAsync(message);
        verify(kafkaProducer).sendAsync("initiate-fund-transfer", "123456", message);

        // Verify that the result of the publishAsync method is the expected SendResult object
        assertEquals(sendResult, result.get());
    }
}
