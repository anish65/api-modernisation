package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.AccountBalanceRS;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/accounts")
@Tag(name = "Account APIs",
        description = "The API is used to expose account api in zand system")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{accountId}/balance")
    public Mono<AccountBalanceRS> getAccountBalance(@PathVariable String accountId) {
        return accountService.getAccountBalance(accountId);
    }

    // API : Used for creating account
    @PostMapping
    public Mono<AccountDetail> createAccount(@RequestBody AccountDetail accountDetail) {
        return accountService.createAccount(accountDetail);
    }

}
