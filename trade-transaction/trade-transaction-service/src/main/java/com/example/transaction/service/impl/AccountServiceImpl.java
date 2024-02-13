package com.example.transaction.service.impl;

import com.example.transaction.mapper.TransactionAccountMapper;
import com.example.transaction.mapper.TransactionBrokerageAccountMapper;
import com.example.transaction.mapper.TransactionBrokerageMapper;
import com.example.transaction.model.dto.TransactionBrokerageAccountDTO;
import com.example.transaction.model.po.TransactionBrokerage;
import com.example.transaction.model.po.TransactionBrokerageAccount;
import com.example.transaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    TransactionBrokerageMapper brokerageMapper;

    @Autowired
    TransactionAccountMapper accountMapper;

    @Autowired
    TransactionBrokerageAccountMapper brokerageAccountMapper;

    private ReentrantLock balanceLock = new ReentrantLock();

    private ReentrantLock brokerageBalanceLock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transfer(String uid, Integer bid, BigDecimal amount) {
        balanceLock.lock();
        try {
            BigDecimal balance = accountMapper.selectBalanceByUserId(uid);
            if (balance.compareTo(amount) < 0) return -1;
            balance = balance.subtract(amount);
            accountMapper.updateBalanceByUserId(uid, balance);
        } finally {
            balanceLock.unlock();
        }
        brokerageBalanceLock.lock();
        try {
            BigDecimal brokerageBalance = brokerageAccountMapper.selectBrokerageBalance(uid, bid);
            if (brokerageBalance == null) brokerageAccountMapper.insertBrokerageAccount(
                    new TransactionBrokerageAccount(null, uid, bid, amount)
            );
            else {
                brokerageBalance = brokerageBalance.add(amount);
                brokerageAccountMapper.updateBrokerageBalance(uid, bid, brokerageBalance);
            }
        } finally {
            brokerageBalanceLock.unlock();
        }
        return 1;
    }

    @Override
    public int withdraw(String uid, Integer bid, BigDecimal amount) {
        brokerageBalanceLock.lock();
        try {
            BigDecimal brokerageBalance = brokerageAccountMapper.selectBrokerageBalance(uid, bid);
            if (brokerageBalance.compareTo(amount) < 0) return -1;
            brokerageBalance = brokerageBalance.subtract(amount);
            brokerageAccountMapper.updateBrokerageBalance(uid, bid, brokerageBalance);
        } finally {
            brokerageBalanceLock.unlock();
        }
        balanceLock.lock();
        try {
            BigDecimal balance = accountMapper.selectBalanceByUserId(uid);
            balance = balance.add(amount);
            accountMapper.updateBalanceByUserId(uid, balance);
        } finally {
            balanceLock.unlock();
        }
        return 1;
    }

    @Override
    public int getDefaultBrokerage(String uid) {
        try {
            return accountMapper.selectDefaultBrokerageByUserId(uid);
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }

    }

    @Override
    public BigDecimal getAccountBalance(String uid) {
        return accountMapper.selectBalanceByUserId(uid);
    }

    @Override
    public List<TransactionBrokerage> getBrokerageList() {
        return brokerageMapper.selectBrokerageList();
    }

    @Override
    public List<TransactionBrokerageAccountDTO> getBrokerageAccountByUserId(String uid) {
        return brokerageAccountMapper.selectBrokerageAccountByUserId(uid);
    }
}
