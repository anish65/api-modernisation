package com.example.legacyservice.controller;

import com.example.legacyservice.dto.TransactionRQ;
import com.example.legacyservice.dto.TransactionRS;
import com.example.legacyservice.exception.ErrorResponse;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static com.example.legacyservice.constants.SwaggerConstants.*;

@RestController
@RequestMapping("/transactions")
@Slf4j
@Tag(name = TRANSACTION_CONTROLLER_TAG_NAME, description = TRANSACTION_CONTROLLER_TAG_DESCRIPTION)
public class TransactionController {

    private Bucket bucket;

    @PostConstruct
    public void init() {
        Bandwidth limit = Bandwidth.classic(7, Refill.greedy(7, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Operation(summary = TRANSACTION_API_SUMMARY, description = TRANSACTION_API_DESCRIPTION)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionRQ.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping()
    public ResponseEntity<TransactionRS> doTransfer(@RequestBody TransactionRQ message) throws InterruptedException {
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        log.info("Received {} message with key: {}, message: {}", message.getTransactionType(), message.getReferenceNo(), message);
        TransactionRS transactionRS = new TransactionRS();
        transactionRS.setReferenceNo(message.getReferenceNo());
        transactionRS.setAccountId(message.getAccountId());
        transactionRS.setStatus("SUCCESS");
        log.info("Completed {} processing : ending message with key: {}, message: {}", message.getTransactionType(), message.getReferenceNo(), transactionRS);
        return ResponseEntity.ok(transactionRS);
    }

}
