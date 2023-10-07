package com.zand.system.transactionprocessor.messaging.consumer;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionprocessor.config.RateLimitConfig;
import com.zand.system.transactionprocessor.service.TxnProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import static org.mockito.Mockito.*;

class FundTransferEventListenerTest {

    @Mock
    private TxnProcessingService txnProcessingService;

    private FundTransferEventListener fundTransferEventListener;

    @Mock
    KafkaListenerEndpointRegistry registry;

    @Mock
    RateLimitConfig rateLimitConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fundTransferEventListener = new FundTransferEventListener(txnProcessingService, registry, rateLimitConfig);
    }

    @Test
    void receive_shouldProcessMessage_whenCalled() throws Exception {
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("12345");

        fundTransferEventListener.receive(message, "key", 0, 0L);

        verify(txnProcessingService, times(1)).process(message);
    }
}
