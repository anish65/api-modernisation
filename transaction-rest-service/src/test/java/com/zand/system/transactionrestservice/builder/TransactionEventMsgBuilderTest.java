package com.zand.system.transactionrestservice.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.dto.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TransactionEventMsgBuilderTest {

    @Test
    public void testMapToTransactionRQMessage() {
        // Create a mock FundTransferRQ object
        TransactionRQ transactionRQ = mock(TransactionRQ.class);
        when(transactionRQ.getAccountId()).thenReturn("987654");
        when(transactionRQ.getAmount()).thenReturn(new BigDecimal(1000.0));
        when(transactionRQ.getCurrency()).thenReturn(Currency.USD);
        when(transactionRQ.getDescription()).thenReturn("Test transfer");

        // Call the mapToFundTransferMessage method
        TransactionRQMessage transactionRQMessage = TransactionEventMsgBuilder.mapToFundTransferMessage(transactionRQ, TransactionType.DEBIT);

        // Verify that the FundTransferRQMessage object was created correctly
        assertEquals("987654", transactionRQMessage.getAccountId());
        assertEquals(new BigDecimal(1000.0), transactionRQMessage.getAmount());
        assertEquals("USD", transactionRQMessage.getCurrency());
        assertEquals("Test transfer", transactionRQMessage.getDescription());
    }
}
