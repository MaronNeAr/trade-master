package com.example.transaction.service;

import com.example.transaction.model.dto.TransactionBrokerageAccountDto;
import com.example.transaction.model.po.TransactionBrokerage;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    int transfer(String uid, Integer bid, BigDecimal amount);

    int withdraw(String uid, Integer bid, BigDecimal amount);

    int getDefaultBrokerage(String uid);

    BigDecimal getAccountBalance(String uid);

    List<TransactionBrokerage> getBrokerageList();

    List<TransactionBrokerageAccountDto> getBrokerageAccountByUserId(String uid);
}
