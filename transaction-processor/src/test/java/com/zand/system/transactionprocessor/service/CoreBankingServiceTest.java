package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class CoreBankingServiceTest {

    private static final String URL = "http://localhost:8088/core-banking/fundTransfer";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CoreBankingService coreBankingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doTransaction_shouldReturnSuccessResponse_whenCoreBankingServiceIsUp() {
        TransactionRQMessage request = new TransactionRQMessage();
        TransactionRSMessage expectedResponse = new TransactionRSMessage();
        expectedResponse.setStatus("SUCCESS");

        when(restTemplate.postForObject(URL, request, TransactionRSMessage.class)).thenReturn(expectedResponse);

        TransactionRSMessage actualResponse = coreBankingService.doTransaction(request);

        assertNotNull("SUCCESS", actualResponse.getStatus());
    }

    @Test
    void doTransaction_shouldReturnErrorResponse_whenCoreBankingServiceIsDown() {
        TransactionRQMessage request = new TransactionRQMessage();
        TransactionRSMessage expectedResponse = new TransactionRSMessage();
        expectedResponse.setStatus("FAILED");
        expectedResponse.setErrorDescription("downstream service is down");

        when(restTemplate.postForObject(URL, request, TransactionRSMessage.class)).thenThrow(new RuntimeException());

        TransactionRSMessage actualResponse = coreBankingService.doTransaction(request);

        assertNotNull("FAILED", actualResponse.getStatus());
    }
}
