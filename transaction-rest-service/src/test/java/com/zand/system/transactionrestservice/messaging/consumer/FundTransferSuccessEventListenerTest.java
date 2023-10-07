package com.zand.system.transactionrestservice.messaging.consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.transactionrestservice.service.TransactionService;
import org.junit.jupiter.api.Test;

public class FundTransferSuccessEventListenerTest {

    @Test
    public void testReceive() {
        // Create a mock FundTransferRSMessage object
        TransactionRSMessage message = new TransactionRSMessage();
        message.setReferenceNo("123456");

        // Create a mock TransactionService object
        TransactionService transactionService = mock(TransactionService.class);

        // Create a new FundTransferSuccessEventListener object
        FundTransferSuccessEventListener fundTransferSuccessEventListener = new FundTransferSuccessEventListener(transactionService);

        // Call the receive method and verify that the transactionService.updateTransactionDetails method was called
        fundTransferSuccessEventListener.receive(message, "test-key", 0, 0L);
        verify(transactionService).updateTransactionDetails(message);
    }
}
