package com.zand.system.transactionrestservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Transaction Request")
public class TransactionRQ {

    @NotEmpty
    @Size(min = 10, max = 10, message = "invalid Account Id")
    @Pattern(regexp="^[0-9]+$", message="invalid Account Id with non numeric characters")
    @Schema(description = "Account Id that using in transaction")
    String accountId;

    @NotNull
    @Schema(description = "Amount using in transaction")
    BigDecimal amount;

    @NotNull
    @Schema(description = "Currency involved in transaction")
    Currency currency;

    @NotEmpty
    @Schema(description = "Narration of the transaction")
    String description;

}
