package com.example.transaction.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionBrokerageAccountDTO {
    private BigDecimal balance;
    private String brokerageName;

    public TransactionBrokerageAccountDTO(BigDecimal balance, String brokerageName) {
        this.balance = balance;
        this.brokerageName = brokerageName;
    }
}
