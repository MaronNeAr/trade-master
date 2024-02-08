package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionAccount {
    private Integer accountId;
    private Integer userId;
    private BigDecimal balance;
    private Integer defaultBrokerage;

    public TransactionAccount(Integer accountId, Integer userId, BigDecimal balance, Integer defaultBrokerage) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.defaultBrokerage = defaultBrokerage;
    }
}
