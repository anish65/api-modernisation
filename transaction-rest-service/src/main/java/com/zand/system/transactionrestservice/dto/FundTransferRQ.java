package com.zand.system.transactionrestservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FundTransferRQ {

    @NotEmpty
    @Pattern(regexp="^[0-9]+$", message="invalid Account Id with non numeric characters")
    String cifId;

    @NotEmpty
    @Size(min = 10, max = 10, message = "invalid Account Id")
    @Pattern(regexp="^[0-9]+$", message="invalid Account Id with non numeric characters")
    String fromAccountId;

    @NotNull
    @NotEmpty
    @Size(min = 10, max = 10, message = "invalid Account Id")
    @Pattern(regexp="^[0-9]+$", message="invalid Account Id with non numeric characters")
    String toAccountId;

    @NotNull
    BigDecimal amount;

    @NotNull
    Currency currency;

    @NotEmpty
    String description;


}


