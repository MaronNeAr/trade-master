package com.example.transaction.model.vo;

import com.example.transaction.model.po.TransactionTask;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailsVo {
    private Integer taskId;
    private String userId;
    private String securityCode;
    private String securityName;
    private Timestamp transactionTime;
    private String transactionType;
    private BigDecimal transactionPrice;
    private Integer transactionVolume;
    private String transactionStatus;
    private BigDecimal currentPrice;

    public TransactionDetailsVo(TransactionTask task, String securityName, BigDecimal currentPrice) {
        this.taskId = task.getTaskId();
        this.userId = task.getUserId();
        this.securityCode = task.getSecurityCode();
        this.securityName = securityName;
        this.transactionTime = task.getTransactionTime();
        this.transactionType = task.getTransactionType();
        this.transactionPrice = task.getTransactionPrice();
        this.transactionVolume = task.getTransactionVolume();
        this.transactionStatus = task.getTransactionStatus();
        this.currentPrice = currentPrice;
    }
}
