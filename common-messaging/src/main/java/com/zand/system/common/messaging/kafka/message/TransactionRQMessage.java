package com.zand.system.common.messaging.kafka.message;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRQMessage implements KafkaMessage {

    String referenceNo;
    String accountId;
    BigDecimal amount;
    String currency;
    String description;
    String transactionType;

}
