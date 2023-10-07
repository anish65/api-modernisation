package com.zand.system.transactionprocessor.messaging.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionprocessor.config.RateLimitConfig;
import com.zand.system.transactionprocessor.service.TxnProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FundTransferEventListenerTest {

    private static final String TOPIC_NAME = "initiate-fund-transfer";
    private static final String GROUP_ID = "test-group";

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
        FundTransferRQMessage message = new FundTransferRQMessage();
        message.setReferenceNo("12345");

        String messageJson = new ObjectMapper().writeValueAsString(message);

        fundTransferEventListener.receive(message, "key", 0, 0L);

        verify(txnProcessingService, times(1)).process(message);
    }
}
