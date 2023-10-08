package com.zand.system.transactionrestservice.builder;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionrestservice.entity.TransactionDetails;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionDetailEntityBuilderTest {

    @Test
    void buildTransactionEntity_shouldReturnTransactionDetails_whenCalled() {
        TransactionRQMessage message = new TransactionRQMessage();
        message.setReferenceNo("12345");
        message.setAccountId("67890");
        message.setAmount(new BigDecimal(100.0));
        message.setCurrency("USD");
        message.setDescription("Test transaction");
        message.setTransactionType("DEBIT");

        TransactionDetails expectedDetails = TransactionDetails.builder()
                .referenceNo("12345")
                .accountId("67890")
                .amount(new BigDecimal(100.0))
                .currency("USD")
                .description("Test transaction")
                .transactionType("DEBIT")
                .transactionStatus("IN_PROGRESS")
                .build();

        TransactionDetails actualDetails = TransactionDetailEntityBuilder.buildTransactionEntity(message);

        assertEquals(expectedDetails.getReferenceNo(), actualDetails.getReferenceNo());
        assertEquals(expectedDetails.getAccountId(), actualDetails.getAccountId());
        assertEquals(expectedDetails.getAmount(), actualDetails.getAmount());
        assertEquals(expectedDetails.getCurrency(), actualDetails.getCurrency());
        assertEquals(expectedDetails.getDescription(), actualDetails.getDescription());
        assertEquals(expectedDetails.getTransactionType(), actualDetails.getTransactionType());
        assertEquals(expectedDetails.getTransactionStatus(), actualDetails.getTransactionStatus());
    }
}
