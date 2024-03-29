package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransactionTask {
    private Integer taskId;
    private String userId;
    private String securityCode;
    private Timestamp transactionTime;
    private String transactionType;
    private BigDecimal transactionPrice;
    private Integer transactionVolume;
    private String transactionStatus;

    public TransactionTask() {
    }

    public TransactionTask(Integer taskId, String userId, String securityCode, Timestamp transactionTime, String transactionType, BigDecimal transactionPrice, Integer transactionVolume, String transactionStatus) {
        this.taskId = taskId;
        this.userId = userId;
        this.securityCode = securityCode;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
        this.transactionPrice = transactionPrice;
        this.transactionVolume = transactionVolume;
        this.transactionStatus = transactionStatus;
    }
}
