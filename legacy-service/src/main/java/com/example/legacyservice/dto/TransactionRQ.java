package com.example.legacyservice.dto;

import com.zand.system.common.messaging.kafka.message.KafkaMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transaction Request")
public class TransactionRQ implements KafkaMessage {

    @Schema(description = "Reference number to the transaction", required = true)
    String referenceNo;
    @Schema(description = "Account Id on transaction is going to happen", required = true)
    String accountId;
    @Schema(description = "Amount of transaction", required = true)
    BigDecimal amount;
    @Schema(description = "Currency on which transaction occurring", required = true)
    String currency;
    @Schema(description = "Narration for the transaction", required = true)
    String description;

    @Schema(description = "Type of transaction", required = true)
    TransactionType transactionType;

}
