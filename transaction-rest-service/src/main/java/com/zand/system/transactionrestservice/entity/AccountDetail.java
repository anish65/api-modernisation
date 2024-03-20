package com.zand.system.transactionrestservice.entity;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Table("account_detail")
public class AccountDetail {

    @Id
    @Column("id")
//    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @Column("cif_id")
    private String cifId;
    @Column("account_Id")
    private String accountId;
    @Column("account_type")
    private String accountType;
    @Column("currency")
    private String currency;
    @Column("account_balance")
    private BigDecimal balance;
    @Column("created_at")
//    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalTime createdAt;

}
