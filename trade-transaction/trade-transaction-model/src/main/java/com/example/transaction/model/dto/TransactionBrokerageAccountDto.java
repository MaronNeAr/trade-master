package com.example.transaction.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionBrokerageAccountDto {
    private BigDecimal balance;
    private String brokerageName;

    public TransactionBrokerageAccountDto(BigDecimal balance, String brokerageName) {
        this.balance = balance;
        this.brokerageName = brokerageName;
    }
}
