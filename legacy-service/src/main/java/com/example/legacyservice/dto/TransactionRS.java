package com.example.legacyservice.dto;

import com.zand.system.common.messaging.kafka.message.KafkaMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transaction Response")
public class TransactionRS implements KafkaMessage {

    @Schema(description = "Reference number provided in the request")
    String referenceNo;
    @Schema(description = "Account Id provided in the request")
    String accountId;
    @Schema(description = "Status of the transaction")
    String status;
    @Schema(description = "If the status if failed. This will have the information about the transaction error")
    String errorDescription;

}
