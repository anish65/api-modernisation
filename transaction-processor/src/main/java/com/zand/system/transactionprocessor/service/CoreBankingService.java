package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreBankingService {

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${legacy.baseUrl}")
    private String baseUrl;

    @Value("${legacy.transaction.path}")
    private String transactionPath;


    //Todo : Retry logic
    public TransactionRSMessage doTransaction(TransactionRQMessage message) {
        try {
            return restTemplate.postForObject(getTransactionUrl(message), message, TransactionRSMessage.class);
        } catch (Exception e) {
            log.error("Error while calling core banking service. Error: {}", e.getMessage());
            TransactionRSMessage fundTransferRSMessage = new TransactionRSMessage();
            fundTransferRSMessage.setReferenceNo(message.getReferenceNo());
            fundTransferRSMessage.setAccountId(message.getAccountId());
            fundTransferRSMessage.setStatus("FAILED");
            fundTransferRSMessage.setErrorDescription("downstream service is down");
            return fundTransferRSMessage;
        }
    }

    private String getTransactionUrl(TransactionRQMessage message) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl+transactionPath)
                .buildAndExpand("DEBIT".equals(message.getTransactionType())?"debit":"credit")
                .toString();
    }

}
