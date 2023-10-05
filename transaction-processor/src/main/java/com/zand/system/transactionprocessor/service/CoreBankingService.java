package com.zand.system.transactionprocessor.service;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoreBankingService {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://localhost:8088/core-banking/fundTransfer";

    //Todo : Retry logic
    public FundTransferRSMessage doFundTransfer(FundTransferRQMessage message) {
        try {
            return restTemplate.postForObject(URL, message, FundTransferRSMessage.class);
        } catch (Exception e) {
            log.error("Error while calling core banking service. Error: {}", e.getMessage());
            FundTransferRSMessage fundTransferRSMessage = new FundTransferRSMessage();
            fundTransferRSMessage.setReferenceNo(message.getReferenceNo());
            fundTransferRSMessage.setCifId(message.getCifId());
            fundTransferRSMessage.setStatus("FAILED");
            fundTransferRSMessage.setErrorDescription("downstream service is down");
            return fundTransferRSMessage;
        }
    }

}
