package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransactionRecord {
    private Integer recordId;
    private String userId;
    private String securityCode;
    private Timestamp transactionDate;
    private String transactionType;
    private BigDecimal transactionPrice;
    private Integer transactionVolume;

    public TransactionRecord() {
    }

    public TransactionRecord(Integer recordId, String userId, String securityCode, Timestamp transactionDate, String transactionType, BigDecimal transactionPrice, Integer transactionVolume) {
        this.recordId = recordId;
        this.userId = userId;
        this.securityCode = securityCode;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.transactionPrice = transactionPrice;
        this.transactionVolume = transactionVolume;
    }
}
