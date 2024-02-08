package com.example.transaction.model.po;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionBrokerageAccount {
    private Integer brokerageAccountId;
    private Integer userId;
    private Integer brokerageId;
    private BigDecimal balance;

    public TransactionBrokerageAccount(Integer brokerageAccountId, Integer userId, Integer brokerageId, BigDecimal balance) {
        this.brokerageAccountId = brokerageAccountId;
        this.userId = userId;
        this.brokerageId = brokerageId;
        this.balance = balance;
    }
}
