package com.zand.system.transactionrestservice.builder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionrestservice.dto.Currency;
import com.zand.system.transactionrestservice.dto.FundTransferRQ;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class FundTransferMessageBuilderTest {

    @Test
    public void testMapToFundTransferMessage() {
        // Create a mock FundTransferRQ object
        FundTransferRQ fundTransferRQ = mock(FundTransferRQ.class);
        when(fundTransferRQ.getCifId()).thenReturn("123456");
        when(fundTransferRQ.getFromAccountId()).thenReturn("987654");
        when(fundTransferRQ.getToAccountId()).thenReturn("456789");
        when(fundTransferRQ.getAmount()).thenReturn(new BigDecimal(1000.0));
        when(fundTransferRQ.getCurrency()).thenReturn(Currency.USD);
        when(fundTransferRQ.getDescription()).thenReturn("Test transfer");

        // Call the mapToFundTransferMessage method
        FundTransferRQMessage fundTransferRQMessage = FundTransferMessageBuilder.mapToFundTransferMessage(fundTransferRQ);

        // Verify that the FundTransferRQMessage object was created correctly
        assertEquals("123456", fundTransferRQMessage.getCifId());
        assertEquals("987654", fundTransferRQMessage.getFromAccountId());
        assertEquals("456789", fundTransferRQMessage.getToAccountId());
        assertEquals(new BigDecimal(1000.0), fundTransferRQMessage.getAmount());
        assertEquals("USD", fundTransferRQMessage.getCurrency());
        assertEquals("Test transfer", fundTransferRQMessage.getDescription());
    }
}
