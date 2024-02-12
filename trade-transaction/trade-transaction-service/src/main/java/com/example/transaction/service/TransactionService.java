package com.example.transaction.service;

import com.example.transaction.model.po.TransactionTask;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.math.BigDecimal;

public interface TransactionService {

    int buy(String uid, String code, BigDecimal price, int volume);

    int sell(String uid, String code, BigDecimal price, int volume);

    int buyCancel(Integer taskId);

    int sellCancel(Integer taskId);

}
