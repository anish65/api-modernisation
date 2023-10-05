package com.zand.system.transactionrestservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountBalanceRS {

    String cifId;
    String accountId;
    BigDecimal balance;
    Currency currency;
}
