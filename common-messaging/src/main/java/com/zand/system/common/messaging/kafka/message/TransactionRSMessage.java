package com.zand.system.common.messaging.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRSMessage implements KafkaMessage {

    String referenceNo;
    String accountId;
    String status;
    String errorDescription;

}
