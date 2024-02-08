package com.example.transaction.service;

import com.example.transaction.model.po.TransactionBrokerage;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    int transfer(Integer uid, Integer bid, BigDecimal amount);

    int withdraw(Integer uid, Integer bid, BigDecimal amount);

    List<TransactionBrokerage> getBrokerageList();
}
