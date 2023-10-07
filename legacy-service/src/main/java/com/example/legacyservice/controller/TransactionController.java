package com.example.legacyservice.controller;

import com.zand.system.common.messaging.kafka.message.FundTransferRQMessage;
import com.zand.system.common.messaging.kafka.message.FundTransferRSMessage;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@Slf4j
public class TransactionController {

    private Bucket bucket;

    @PostConstruct
    public void init() {
        Bandwidth limit = Bandwidth.classic(12, Refill.greedy(12, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/fundTransfer")
    public ResponseEntity<FundTransferRSMessage> doFundTransfer(@RequestBody FundTransferRQMessage message) throws InterruptedException {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        log.info("Received message with key: {}, message: {}", message.getReferenceNo(), message);
        FundTransferRSMessage fundTransferRSMessage = new FundTransferRSMessage();
        fundTransferRSMessage.setReferenceNo(message.getReferenceNo());
        fundTransferRSMessage.setCifId(message.getCifId());
        if(!ObjectUtils.isEmpty(message.getReferenceNo()) && isEven(message.getReferenceNo())) {
                fundTransferRSMessage.setStatus("SUCCESS");
        } else {
            fundTransferRSMessage.setStatus("FAILED");
            fundTransferRSMessage.setErrorDescription("no sufficient balance");
        }
        log.info("Completed processing : ending message with key: {}, message: {}", message.getReferenceNo(), fundTransferRSMessage);
        return ResponseEntity.ok(fundTransferRSMessage);
    }

    private boolean isEven(String referenceNo) {
        return Integer.valueOf(referenceNo.substring(referenceNo.length()-1)) % 2 == 0;
    }

}
