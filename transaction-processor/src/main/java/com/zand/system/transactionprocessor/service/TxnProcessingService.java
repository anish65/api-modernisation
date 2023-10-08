package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import com.zand.system.transactionprocessor.messaging.publisher.FundTxnFailureEventPublisher;
import com.zand.system.transactionprocessor.messaging.publisher.FundTxnSuccessEventPublisher;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TxnProcessingService {

    @Autowired
    private FundTxnSuccessEventPublisher successEventPublisher;

    @Autowired
    private FundTxnFailureEventPublisher failureEventPublisher;

    @Autowired
    private CoreBankingService coreBankingService;

    public void process(TransactionRQMessage message) {
        TransactionRSMessage transactionRSMessage = coreBankingService.doTransaction(message);
        if(transactionRSMessage.getStatus().equalsIgnoreCase("FAILED")) {
            failureEventPublisher.publish(transactionRSMessage);
            return;
        }
        successEventPublisher.publish(transactionRSMessage);
    }

}
