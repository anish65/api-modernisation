package com.zand.system.transactionrestservice.builder;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.transactionrestservice.dto.FundTransferRQ;
import com.zand.system.transactionrestservice.dto.TransactionType;

public class FundTransferMessageBuilder {

    public static FundTransferRQMessage mapToFundTransferMessage(FundTransferRQ fundTransferRQ) {
        String fundTransferRefNo = String.valueOf(RandomUtil.getPositiveInt());
        FundTransferRQMessage fundTransferRQMessage = new FundTransferRQMessage();
        fundTransferRQMessage.setReferenceNo(fundTransferRefNo);
        fundTransferRQMessage.setCifId(fundTransferRQ.getCifId());
        fundTransferRQMessage.setFromAccountId(fundTransferRQ.getFromAccountId());
        fundTransferRQMessage.setToAccountId(fundTransferRQ.getToAccountId());
        fundTransferRQMessage.setAmount(fundTransferRQ.getAmount());
        fundTransferRQMessage.setCurrency(fundTransferRQ.getCurrency().name());
        fundTransferRQMessage.setDescription(fundTransferRQ.getDescription());
        return fundTransferRQMessage;
    }
}
