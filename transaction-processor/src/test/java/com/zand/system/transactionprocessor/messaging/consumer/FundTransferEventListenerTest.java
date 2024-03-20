package com.zand.system.transactionprocessor.messaging.consumer;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionprocessor.config.RateLimitConfig;
import com.zand.system.transactionprocessor.service.TxnProcessingService;
import io.github.bucket4j.local.SynchronizedBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;

import static org.mockito.Mockito.*;

class FundTransferEventListenerTest {
    private static final String TOPIC_NAME = "initiate-fund-transfer";
    private static final String GROUP_ID = "test-group";

    @Mock
    private TxnProcessingService txnProcessingService;

    @Mock
    private KafkaListenerEndpointRegistry registry;

    @Mock
    private MessageListenerContainer listenerContainer;

    @Mock
    private RateLimitConfig rateLimitConfig;

    @Mock
    private SynchronizedBucket synchronizedBucket;

    private FundTransferEventListener fundTransferEventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(registry.getListenerContainer(any())).thenReturn(listenerContainer);
        fundTransferEventListener = new FundTransferEventListener(txnProcessingService, registry, rateLimitConfig);
    }

    @Test
    void receive_shouldProcessMessage_whenRateLimitNotExceeded() throws Exception {
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("12345");

        when(synchronizedBucket.tryConsume(anyLong())).thenReturn(true);
        when(rateLimitConfig.resolveBucket(any())).thenReturn(synchronizedBucket);

        fundTransferEventListener.receive(message, "key", 0, 0L);

        verify(txnProcessingService, times(1)).process(message);
        verify(listenerContainer, never()).pause();
        verify(listenerContainer, never()).resume();
    }

    @Test
    void receive_shouldProcessMessage_whenRateLimitExceededAndResolved() throws Exception {
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("12345");

        when(synchronizedBucket.tryConsume(anyLong())).thenReturn(false).thenReturn(true);
        when(rateLimitConfig.resolveBucket(any())).thenReturn(synchronizedBucket);

        fundTransferEventListener.receive(message, "key", 0, 0L);

        verify(txnProcessingService, times(1)).process(message);
        verify(listenerContainer, times(1)).pause();
        verify(listenerContainer, times(1)).resume();
    }

    @Test
    void receive_shouldProcessMessage_whenRateLimitExceededAndResolvedAfterRetry() throws Exception {
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("12345");

        when(synchronizedBucket.tryConsume(anyLong())).thenReturn(false).thenReturn(true);
        when(rateLimitConfig.resolveBucket(any())).thenReturn(synchronizedBucket);

        fundTransferEventListener.receive(message, "key", 0, 0L);

        verify(txnProcessingService, times(1)).process(message);
        verify(listenerContainer, times(1)).pause();
        verify(listenerContainer, times(1)).resume();
    }

}
