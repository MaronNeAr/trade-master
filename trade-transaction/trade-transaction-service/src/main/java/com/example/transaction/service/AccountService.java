package com.example.transaction.service;

import com.example.transaction.model.dto.TransactionBrokerageAccountDTO;
import com.example.transaction.model.po.TransactionBrokerage;
import com.example.transaction.model.po.TransactionBrokerageAccount;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    int transfer(String uid, Integer bid, BigDecimal amount);

    int withdraw(String uid, Integer bid, BigDecimal amount);

    int getDefaultBrokerage(String uid);

    BigDecimal getAccountBalance(String uid);

    List<TransactionBrokerage> getBrokerageList();

    List<TransactionBrokerageAccountDTO> getBrokerageAccountByUserId(String uid);
}
