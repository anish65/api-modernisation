package com.example.legacyservice.controller;

import com.zand.system.common.messaging.kafka.message.TransactionRQMessage;
import com.zand.system.common.messaging.kafka.message.TransactionRSMessage;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/transactions")
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

    @PostMapping("/debit")
    public ResponseEntity<TransactionRSMessage> doDebitTransfer(@RequestBody TransactionRQMessage message) throws InterruptedException {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        log.info("Received debit message with key: {}, message: {}", message.getReferenceNo(), message);
        TransactionRSMessage transactionRSMessage = new TransactionRSMessage();
        transactionRSMessage.setReferenceNo(message.getReferenceNo());
        transactionRSMessage.setAccountId(message.getAccountId());
        transactionRSMessage.setStatus("SUCCESS");
        log.info("Completed debit processing : ending message with key: {}, message: {}", message.getReferenceNo(), transactionRSMessage);
        return ResponseEntity.ok(transactionRSMessage);
    }

    @PostMapping("/credit")
    public ResponseEntity<TransactionRSMessage> doCreditTransfer(@RequestBody TransactionRQMessage message) throws InterruptedException {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        log.info("Received credit message with key: {}, message: {}", message.getReferenceNo(), message);
        TransactionRSMessage transactionRSMessage = new TransactionRSMessage();
        transactionRSMessage.setReferenceNo(message.getReferenceNo());
        transactionRSMessage.setAccountId(message.getAccountId());
        transactionRSMessage.setStatus("SUCCESS");
        log.info("Completed credit processing : ending message with key: {}, message: {}", message.getReferenceNo(), transactionRSMessage);
        return ResponseEntity.ok(transactionRSMessage);
    }

}
