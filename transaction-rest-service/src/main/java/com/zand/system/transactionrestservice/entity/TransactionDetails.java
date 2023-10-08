package com.zand.system.transactionrestservice.entity;

import com.zand.system.transactionrestservice.dto.Currency;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Table("transaction_detail")
public class TransactionDetails {

    @Id
    @Column("id")
    private Long id;
    @Column("reference_no")
    private String referenceNo;
    @Column("account_id")
    private String accountId;
    @Column("amount")
    private BigDecimal amount;
    @Column("currency")
    private String currency;
    @Column("description")
    private String description;
    @Column("transaction_type")
    private String transactionType;
    @Column("transaction_status")
    private String transactionStatus;
    @Column("error_description")
    private String errorDescription;
    @Column("created_at")
    private LocalTime createdAt;
}
