package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
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

    @SneakyThrows
    public void process(FundTransferRQMessage message) {
        FundTransferRSMessage fundTransferRSMessage = coreBankingService.doFundTransfer(message);
        if(fundTransferRSMessage.getStatus().equalsIgnoreCase("FAILED")) {
            failureEventPublisher.publish(fundTransferRSMessage);
            return;
        }
        successEventPublisher.publish(fundTransferRSMessage);
    }

}
