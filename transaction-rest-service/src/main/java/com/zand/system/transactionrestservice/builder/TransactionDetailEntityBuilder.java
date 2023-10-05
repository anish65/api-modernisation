package com.zand.system.transactionrestservice.builder;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionrestservice.entity.TransactionDetails;

public class TransactionDetailEntityBuilder {

    public static TransactionDetails buildTransactionEntity(FundTransferRQMessage fundTransferRQMessage) {
        return TransactionDetails.builder()
                .referenceNo(fundTransferRQMessage.getReferenceNo())
                .cifId(fundTransferRQMessage.getCifId())
                .fromAccountId(fundTransferRQMessage.getFromAccountId())
                .toAccountId(fundTransferRQMessage.getToAccountId())
                .amount(fundTransferRQMessage.getAmount())
                .currency(fundTransferRQMessage.getCurrency())
                .description(fundTransferRQMessage.getDescription())
                .transactionType(fundTransferRQMessage.getTransactionType())
                .transactionStatus("IN_PROGRESS")
                .build();
    }
}
