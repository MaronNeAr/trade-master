package com.example.transaction.service;

import com.example.transaction.model.po.TransactionPosition;
import com.example.transaction.model.po.TransactionTask;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    int buy(String uid, String code, BigDecimal price, int volume);

    int sell(String uid, String code, BigDecimal price, int volume);

    int buyCancel(Integer taskId);

    int sellCancel(Integer taskId);

    String getTaskType(Integer tid);

    List<TransactionPosition> getActivePositions(String uid) throws InterruptedException;

    List<TransactionTask> getAllTasks(String uid) throws InterruptedException;

    List<TransactionTask> getTasksByStatus(String uid, String status) throws InterruptedException;
}
