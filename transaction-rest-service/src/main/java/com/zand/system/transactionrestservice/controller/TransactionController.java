package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.TransactionRS;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/transactions")
@Tag(name = "Transaction APIs",
        description = "The API is used to expose transaction api in zand system")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/debit")
    public Mono<TransactionRS> doDebitTransaction(@Validated @RequestBody TransactionRQ transactionRQ) {
        return transactionService.doDebitTransaction(transactionRQ);
    }

    @PostMapping("/credit")
    public Mono<TransactionRS> doCreditTransaction(@Validated @RequestBody TransactionRQ transactionRQ) {
        return transactionService.doCreditTransaction(transactionRQ);
    }

}
