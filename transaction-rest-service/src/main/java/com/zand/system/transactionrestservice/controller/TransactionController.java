package com.zand.system.transactionrestservice.controller;

import com.zand.system.transactionrestservice.dto.TransactionRS;
import com.zand.system.transactionrestservice.dto.TransactionRQ;
import com.zand.system.transactionrestservice.service.TransactionService;
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
@RequestMapping("/v1/transactions")
@Tag(name = TRANSACTION_CONTROLLER_TAG_NAME, description = TRANSACTION_CONTROLLER_TAG_DESCRIPTION)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Operation(summary = DEBIT_TRANSACTION_API_SUMMARY, description = DEBIT_TRANSACTION_API_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionRQ.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TransactionRS.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/debit")
    public Mono<TransactionRS> doDebitTransaction(@Validated @RequestBody TransactionRQ transactionRQ) {
        return transactionService.doDebitTransaction(transactionRQ);
    }

    @Operation(summary = CREDIT_TRANSACTION_API_SUMMARY, description = CREDIT_TRANSACTION_API_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionRQ.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TransactionRS.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/credit")
    public Mono<TransactionRS> doCreditTransaction(@Validated @RequestBody TransactionRQ transactionRQ) {
        return transactionService.doCreditTransaction(transactionRQ);
    }

}
