package com.example.transaction.model.po;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionBrokerageAccount {
    private Integer brokerageAccountId;
    private String userId;
    private Integer brokerageId;
    private BigDecimal balance;

    public TransactionBrokerageAccount(Integer brokerageAccountId, String userId, Integer brokerageId, BigDecimal balance) {
        this.brokerageAccountId = brokerageAccountId;
        this.userId = userId;
        this.brokerageId = brokerageId;
        this.balance = balance;
    }
}
