package com.zand.system.transactionrestservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaction Response")
public class TransactionRS {

    @Schema(description = "Reference number for this transaction")
    String referenceNo;

    @Schema(description = "Status of the transaction")
    Status status;

    public enum Status {
        SUCCESS, FAILED
    }

}


