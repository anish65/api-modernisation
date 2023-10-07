package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void doFundTransfer_shouldReturnSuccessResponse_whenCoreBankingServiceIsUp() {
        FundTransferRQMessage request = new FundTransferRQMessage();
        FundTransferRSMessage expectedResponse = new FundTransferRSMessage();
        expectedResponse.setStatus("SUCCESS");

        when(restTemplate.postForObject(URL, request, FundTransferRSMessage.class)).thenReturn(expectedResponse);

        FundTransferRSMessage actualResponse = coreBankingService.doFundTransfer(request);

        assertNotNull("SUCCESS", actualResponse.getStatus());
    }

    @Test
    void doFundTransfer_shouldReturnErrorResponse_whenCoreBankingServiceIsDown() {
        FundTransferRQMessage request = new FundTransferRQMessage();
        FundTransferRSMessage expectedResponse = new FundTransferRSMessage();
        expectedResponse.setStatus("FAILED");
        expectedResponse.setErrorDescription("downstream service is down");

        when(restTemplate.postForObject(URL, request, FundTransferRSMessage.class)).thenThrow(new RuntimeException());

        FundTransferRSMessage actualResponse = coreBankingService.doFundTransfer(request);

        assertNotNull("FAILED", actualResponse.getStatus());
    }
}
