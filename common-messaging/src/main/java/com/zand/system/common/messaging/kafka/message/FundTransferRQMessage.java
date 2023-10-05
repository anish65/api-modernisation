package com.zand.system.common.messaging.kafka.message;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferRQMessage implements KafkaMessage {

    String referenceNo;
    String cifId;
    String fromAccountId;
    String toAccountId;
    BigDecimal amount;
    String currency;
    String description;
    String transactionType;

}
