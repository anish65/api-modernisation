package com.zand.system.transactionrestservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Account Balance Response")
public class AccountBalanceRS {

    @Schema(description = "Customer Identification Id")
    String cifId;
    @Schema(description = "Id of the account")
    String accountId;
    @Schema(description = "Balance in the account")
    BigDecimal balance;
    @Schema(description = "Account currency")
    Currency currency;
}
