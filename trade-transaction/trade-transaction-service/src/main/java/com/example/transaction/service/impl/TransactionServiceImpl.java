package com.example.transaction.service.impl;

import com.example.transaction.mapper.TransactionAccountMapper;
import com.example.transaction.mapper.TransactionBrokerageAccountMapper;
import com.example.transaction.mapper.TransactionPositionMapper;
import com.example.transaction.mapper.TransactionTaskMapper;
import com.example.transaction.model.po.TransactionPosition;
import com.example.transaction.model.po.TransactionTask;
import com.example.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionTaskMapper taskMapper;

    @Autowired
    TransactionAccountMapper accountMapper;

    @Autowired
    TransactionBrokerageAccountMapper brokerageAccountMapper;

    @Autowired
    TransactionPositionMapper positionMapper;

    private ReentrantLock balanceLock = new ReentrantLock();

    private ReentrantLock positionLock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buy(String uid, String code, BigDecimal price, int volume) {
        BigDecimal cost = price.multiply(new BigDecimal(volume));
        int defaultBid = accountMapper.selectDefaultBrokerageByUserId(uid);

        balanceLock.lock();
        try{
            BigDecimal balance = brokerageAccountMapper.selectBrokerageBalance(uid, defaultBid);
            if (balance.compareTo(cost) < 0) return -1;
            balance = balance.subtract(cost);
            brokerageAccountMapper.updateBrokerageBalance(uid, defaultBid, balance);
        } finally {
            balanceLock.unlock();
        }
        taskMapper.insertTask(new TransactionTask(null, uid, code, new Timestamp(System.currentTimeMillis()), "buy", price, volume, "active"));
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sell(String uid, String code, BigDecimal price, int volume) {
        positionLock.lock();
        try{
            TransactionPosition position = positionMapper.selectPositionByUserIdAndCode(uid, code);
            if (position == null || position.getPositionQuantity() < volume) return -1;
            else if (position.getPositionQuantity() == volume) positionMapper.updatePositionStatus(position.getPositionId(), "unactive");
            BigDecimal cost = position.getPositionCost();
            cost = cost.subtract(
                    cost.multiply(new BigDecimal(volume))
                            .divide(new BigDecimal(position.getPositionQuantity()))

            );
            position.setPositionQuantity(position.getPositionQuantity() - volume);
            position.setPositionCost(cost);
            positionMapper.updatePosition(position);
        } finally {
            positionLock.unlock();
        }
        taskMapper.insertTask(new TransactionTask(null, uid, code, new Timestamp(System.currentTimeMillis()), "sell", price, volume, "active"));
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buyCancel(Integer taskId) {
        TransactionTask task = taskMapper.selectTaskByTaskId(taskId);
        if (task.getTransactionStatus().equals("cancelled")) return -1;
        BigDecimal cost = task.getTransactionPrice().multiply(new BigDecimal(task.getTransactionVolume()));
        int defaultBid = accountMapper.selectDefaultBrokerageByUserId(task.getUserId());

        balanceLock.lock();
        try {
            BigDecimal balance = brokerageAccountMapper.selectBrokerageBalance(task.getUserId(), defaultBid);
            balance = balance.add(cost);
            brokerageAccountMapper.updateBrokerageBalance(task.getUserId(),  defaultBid, balance);
        } finally {
            balanceLock.unlock();
        }
        taskMapper.updateTaskStatusById(task.getTaskId(), "cancelled");
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int sellCancel(Integer taskId) {
        TransactionTask task = taskMapper.selectTaskByTaskId(taskId);
        if (task.getTransactionStatus().equals("cancelled")) return -1;

        positionLock.lock();
        try {
            TransactionPosition position = positionMapper.selectPositionByUserIdAndCode(task.getUserId(), task.getSecurityCode());
            BigDecimal cost = position.getPositionCost();
            cost = cost.add(
                    cost.multiply(new BigDecimal(task.getTransactionVolume()))
                            .divide(new BigDecimal(position.getPositionQuantity())
                                    .add(new BigDecimal(task.getTransactionVolume())))

            );
            position.setPositionQuantity(position.getPositionQuantity() + task.getTransactionVolume());
            position.setPositionCost(cost);
            positionMapper.updatePosition(position);
        } finally {
            positionLock.unlock();
        }
        taskMapper.insertTask(task);
        return 1;
    }

    @Override
    public List<TransactionPosition> getActivePositions(String uid) {
        return positionMapper.selectPositionListByUserIdAndStatus(uid,"active");
    }

    @Override
    public List<TransactionTask> getAllTasks(String uid) {
        return taskMapper.selectTaskListByUserId(uid);
    }

    @Override
    public List<TransactionTask> getTasksByStatus(String uid, String status) {
        return taskMapper.selectTaskListByUserIdAndStatus(uid, status);
    }
}
