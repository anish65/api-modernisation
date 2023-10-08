package com.zand.system.transactionrestservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Schema(description = "Transaction Response")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRS {

    @Schema(description = "Reference number for this transaction")
    String referenceNo;

    @Schema(description = "Status of the transaction")
    Status status;

    public enum Status {
        SUCCESS, FAILED
    }

}


