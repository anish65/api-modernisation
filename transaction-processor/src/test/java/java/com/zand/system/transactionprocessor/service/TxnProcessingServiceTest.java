package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
import com.zand.system.transactionprocessor.messaging.publisher.FundTxnFailureEventPublisher;
import com.zand.system.transactionprocessor.messaging.publisher.FundTxnSuccessEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class TxnProcessingServiceTest {

    @Mock
    private FundTxnSuccessEventPublisher successEventPublisher;

    @Mock
    private FundTxnFailureEventPublisher failureEventPublisher;

    @Mock
    private CoreBankingService coreBankingService;

    @InjectMocks
    private TxnProcessingService txnProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void process_shouldPublishSuccessEvent_whenFundTransferIsSuccessful() {
        FundTransferRQMessage request = new FundTransferRQMessage();
        FundTransferRSMessage response = new FundTransferRSMessage();
        response.setStatus("SUCCESS");

        when(coreBankingService.doFundTransfer(request)).thenReturn(response);

        txnProcessingService.process(request);

        verify(successEventPublisher, times(1)).publish(response);
        verify(failureEventPublisher, never()).publish(any());
    }

    @Test
    void process_shouldPublishFailureEvent_whenFundTransferFails() {
        FundTransferRQMessage request = new FundTransferRQMessage();
        FundTransferRSMessage response = new FundTransferRSMessage();
        response.setStatus("FAILED");

        when(coreBankingService.doFundTransfer(request)).thenReturn(response);

        txnProcessingService.process(request);

        verify(failureEventPublisher, times(1)).publish(response);
        verify(successEventPublisher, never()).publish(any());
    }
}
