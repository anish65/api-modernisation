package com.zand.system.transactionrestservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRQ {

    @NotEmpty
    @Size(min = 10, max = 10, message = "invalid Account Id")
    @Pattern(regexp="^[0-9]+$", message="invalid Account Id with non numeric characters")
    String accountId;

    @NotNull
    BigDecimal amount;

    @NotNull
    Currency currency;

    @NotEmpty
    String description;

}
