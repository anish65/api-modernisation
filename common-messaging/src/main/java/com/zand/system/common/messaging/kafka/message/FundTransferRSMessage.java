package com.zand.system.common.messaging.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FundTransferRSMessage implements KafkaMessage {

    String referenceNo;
    String cifId;
    String status;
    String errorDescription;

}
