package com.zand.system.transactionrestservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRS {

    String referenceNo;
    Status status;

    public enum Status {
        SUCCESS, FAILED
    }

}


