package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransactionTask {
    private Integer taskId;
    private Integer userId;
    private String securityCode;
    private Timestamp transactionDate;
    private String transactionType;
    private BigDecimal transactionPrice;
    private Integer transactionVolume;
    private String transactionStatus;

    public TransactionTask() {
    }

    public TransactionTask(Integer taskId, Integer userId, String securityCode, Timestamp transactionDate, String transactionType, BigDecimal transactionPrice, Integer transactionVolume, String transactionStatus) {
        this.taskId = taskId;
        this.userId = userId;
        this.securityCode = securityCode;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.transactionPrice = transactionPrice;
        this.transactionVolume = transactionVolume;
        this.transactionStatus = transactionStatus;
    }
}
