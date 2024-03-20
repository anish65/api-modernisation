package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.AccountBalanceRS;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.dto.TransactionRS;
import com.zand.system.transactionrestservice.entity.AccountDetail;
import com.zand.system.transactionrestservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.zand.system.transactionrestservice.constant.SwaggerConstants.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/accounts")
@Tag(name = ACCOUNT_CONTROLLER_TAG_NAME, description = ACCOUNT_CONTROLLER_TAG_DESCRIPTION)
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = ACCOUNT_BALANCE_API_SUMMARY, description = ACCOUNT_BALANCE_API_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionRQ.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TransactionRS.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/{accountId}/balance")
    public Mono<AccountBalanceRS> getAccountBalance(@PathVariable String accountId) {
        return accountService.getAccountBalance(accountId);
    }

    @Operation(summary = ACCOUNT_CREATE_API_SUMMARY, description = ACCOUNT_CREATE_API_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDetail.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public Mono<AccountDetail> createAccount(@RequestBody AccountDetail accountDetail) {
        return accountService.createAccount(accountDetail);
    }

}
