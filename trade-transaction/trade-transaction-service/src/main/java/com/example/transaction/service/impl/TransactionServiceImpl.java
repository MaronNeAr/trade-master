package com.example.transaction.service.impl;

import com.example.transaction.mapper.*;
import com.example.transaction.model.po.TransactionPosition;
import com.example.transaction.model.po.TransactionTask;
import com.example.transaction.service.MarketService;
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

    @Autowired
    MarketService marketService;

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
    public String getTaskType(Integer tid) {
        return taskMapper.selectTaskTypeByTaskId(tid);
    }

    @Override
    public List<TransactionPosition> getActivePositions(String uid) throws InterruptedException {
        return positionMapper.selectPositionListByUserIdAndStatus(uid,"active");
//        List<TransactionPosition> positionList = positionMapper.selectPositionListByUserIdAndStatus(uid,"active");
//        List<TransactionPositionVo> resList = new ArrayList<>();
//        CountDownLatch latch = new CountDownLatch(positionList.size());
//
//        positionList.forEach((position) -> {
//            new Thread(() -> {
//                StockQuote stock = new StockQuote();
//                try {
//                    stock = marketService.getMarketData(position.getSecurityCode());
//                } catch (UnsupportedEncodingException e) {
//                    System.out.println(e);
//                }
//                resList.add(new TransactionPositionVo(position, stock.getName(), stock.getLastPrice()));
//                latch.countDown();
//            }).start();
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        return resList.stream().sorted((o1, o2) -> o2.getPositionCost().compareTo(o1.getPositionCost())).collect(Collectors.toList());
    }

    @Override
    public List<TransactionTask> getAllTasks(String uid) throws InterruptedException {
        return taskMapper.selectTaskListByUserId(uid);
//        List<TransactionTask> taskList = taskMapper.selectTaskListByUserId(uid);
//        List<TransactionDetailsVo> resList = new ArrayList<>();
//        CountDownLatch latch = new CountDownLatch(taskList.size());
//
//        taskList.forEach((task) -> {
//            new Thread(() -> {
//                StockQuote stock = new StockQuote();
//                try {
//                    stock = marketService.getMarketData(task.getSecurityCode());
//                } catch (UnsupportedEncodingException e) {
//                    System.out.println(e);
//                }
//                resList.add(new TransactionDetailsVo(task, stock.getName(), stock.getLastPrice()));
//                latch.countDown();
//            }).start();
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        return resList.stream().sorted((o1, o2) -> o2.getTransactionTime().compareTo(o1.getTransactionTime())).collect(Collectors.toList());
    }

    @Override
    public List<TransactionTask> getTasksByStatus(String uid, String status) throws InterruptedException {
        return taskMapper.selectTaskListByUserIdAndStatus(uid, status);
//        List<TransactionTask> taskList = taskMapper.selectTaskListByUserIdAndStatus(uid, status);
//        List<TransactionDetailsVo> resList = new ArrayList<>();
//        CountDownLatch latch = new CountDownLatch(taskList.size());
//
//        taskList.forEach((task) -> {
//            new Thread(() -> {
//                StockQuote stock = new StockQuote();
//                try {
//                    stock = marketService.getMarketData(task.getSecurityCode());
//                } catch (UnsupportedEncodingException e) {
//                    System.out.println(e);
//                }
//                resList.add(new TransactionDetailsVo(task, stock.getName(), stock.getLastPrice()));
//                latch.countDown();
//            }).start();
//        });
//
//        latch.await(5, TimeUnit.SECONDS);
//        return resList.stream().sorted((o1, o2) -> o2.getTransactionTime().compareTo(o1.getTransactionTime())).collect(Collectors.toList());
    }
}
