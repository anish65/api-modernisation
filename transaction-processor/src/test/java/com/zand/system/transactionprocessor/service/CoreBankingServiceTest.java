package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CoreBankingServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoreBankingService coreBankingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(coreBankingService, "baseUrl", "http://baseUrl");
        ReflectionTestUtils.setField(coreBankingService, "transactionPath", "/transactionPath");
    }

    @Test
    void doTransaction_shouldReturnSuccessResponse_whenCoreBankingServiceIsUp() {
        TransactionRQMessage request = new TransactionRQMessage();
        TransactionRSMessage expectedResponse = new TransactionRSMessage();
        expectedResponse.setStatus("SUCCESS");

        when(restTemplate.postForObject(anyString(), any(TransactionRQMessage.class), any()))
                .thenReturn(expectedResponse);

        TransactionRSMessage actualResponse = coreBankingService.doTransaction(request);

        assertEquals(expectedResponse, actualResponse);
        assertNotNull("SUCCESS", actualResponse.getStatus());
    }

    @Test()
    void doTransaction_shouldReturnErrorResponse_whenCoreBankingServiceIsDown() {
        TransactionRQMessage request = new TransactionRQMessage();
        TransactionRSMessage expectedResponse = new TransactionRSMessage();
        expectedResponse.setStatus("FAILED");
        expectedResponse.setErrorDescription("downstream service is down");

        when(restTemplate.postForObject(anyString(), any(TransactionRQMessage.class), any()))
                .thenThrow(new RuntimeException("test"));

        TransactionRSMessage actualResponse = coreBankingService.doTransaction(request);

        assertNotNull("FAILED", actualResponse.getStatus());
        assertNotNull("downstream service is down", actualResponse.getErrorDescription());
    }
}
