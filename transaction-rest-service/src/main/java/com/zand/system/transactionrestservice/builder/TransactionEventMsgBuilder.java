package com.zand.system.transactionrestservice.builder;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.dto.TransactionType;

public class TransactionEventMsgBuilder {

    public static TransactionRQMessage mapToFundTransferMessage(TransactionRQ transactionRQ, TransactionType transactionType) {
        String fundTransferRefNo = String.valueOf(RandomUtil.getPositiveInt());
        TransactionRQMessage transactionRQMessage = new TransactionRQMessage();
        transactionRQMessage.setReferenceNo(fundTransferRefNo);
        transactionRQMessage.setAccountId(transactionRQ.getAccountId());
        transactionRQMessage.setAmount(transactionRQ.getAmount());
        transactionRQMessage.setCurrency(transactionRQ.getCurrency().name());
        transactionRQMessage.setDescription(transactionRQ.getDescription());
        transactionRQMessage.setTransactionType(transactionType.name());
        return transactionRQMessage;
    }
}
