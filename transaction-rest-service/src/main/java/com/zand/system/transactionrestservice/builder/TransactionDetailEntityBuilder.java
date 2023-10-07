package com.zand.system.transactionrestservice.builder;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionrestservice.entity.TransactionDetails;

public class TransactionDetailEntityBuilder {

    public static TransactionDetails buildTransactionEntity(TransactionRQMessage transactionRQMessage) {
        return TransactionDetails.builder()
                .referenceNo(transactionRQMessage.getReferenceNo())
                .accountId(transactionRQMessage.getAccountId())
                .amount(transactionRQMessage.getAmount())
                .currency(transactionRQMessage.getCurrency())
                .description(transactionRQMessage.getDescription())
                .transactionType(transactionRQMessage.getTransactionType())
                .transactionStatus("IN_PROGRESS")
                .build();
    }
}
