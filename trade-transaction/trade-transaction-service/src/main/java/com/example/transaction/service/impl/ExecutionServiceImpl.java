package com.example.transaction.service.impl;

import com.example.transaction.mapper.TransactionAccountMapper;
import com.example.transaction.mapper.TransactionBrokerageAccountMapper;
import com.example.transaction.mapper.TransactionPositionMapper;
import com.example.transaction.mapper.TransactionTaskMapper;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionPosition;
import com.example.transaction.model.po.TransactionTask;
import com.example.transaction.service.ExecutionService;
import com.example.transaction.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    @Autowired
    TransactionTaskMapper taskMapper;

    @Autowired
    TransactionPositionMapper positionMapper;

    @Autowired
    TransactionAccountMapper accountMapper;

    @Autowired
    TransactionBrokerageAccountMapper brokerageAccountMapper;

    @Autowired
    MarketService marketService;

    private ReentrantLock positionLock = new ReentrantLock();

    private ReentrantLock balanceLock = new ReentrantLock();

    @Override
//    @Scheduled(fixedRate = 3000)
    public int executeBuy() {
        System.out.println("正在轮询处理买入委托订单");
        if (!isAStockTradingTime()) return -1;
        int count = 0;
        List<TransactionTask> taskList = taskMapper.selectTaskListByStatusAndType("active", "buy");
        for (TransactionTask task: taskList) {
            System.out.println(task);
            try {
                StockQuote stock = marketService.getMarketData(task.getSecurityCode());
                if (stock.getSell1().compareTo(task.getTransactionPrice()) > 0) continue;
                positionLock.lock();
                count += buyTransaction(task)? 1: 0;
                positionLock.unlock();
            } catch (Exception e) {
                System.out.println(e);
                positionLock.unlock();
            }
        }

        if (count > 0) System.out.println("成交" + count + "笔买入委托");
        return count;
    }

    @Override
//    @Scheduled(fixedRate = 3000)
    public int executeSell() {
        System.out.println("正在轮询处理卖出委托订单");
        if (!isAStockTradingTime()) return -1;
        int count = 0;
        List<TransactionTask> taskList = taskMapper.selectTaskListByStatusAndType("active", "sell");
        for (TransactionTask task: taskList) {
            System.out.println(task);
            try {
                StockQuote stock = marketService.getMarketData(task.getSecurityCode());
                if (stock.getBuy1().compareTo(task.getTransactionPrice()) < 0) continue;
                balanceLock.lock();
                count += sellTransaction(task)? 1: 0;
                balanceLock.unlock();
            } catch (Exception e) {
                System.out.println(e);
                balanceLock.unlock();
            }
        }

        if (count > 0) System.out.println("成交" + count + "笔卖出委托");
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    private boolean buyTransaction(TransactionTask task) {
        TransactionPosition position = positionMapper.selectPositionByUserIdAndCode(task.getUserId(), task.getSecurityCode());
        if (position == null) {
            BigDecimal cost = task.getTransactionPrice().multiply(
                    new BigDecimal(task.getTransactionVolume())
            );
            positionMapper.insertPosition(new TransactionPosition(null, task.getUserId(),
                    task.getSecurityCode(), task.getTransactionVolume(), cost, "active"));
        } else {
            int quantity = position.getPositionQuantity() + task.getTransactionVolume();
            BigDecimal cost = position.getPositionCost().add(
                    task.getTransactionPrice().multiply(
                            new BigDecimal(task.getTransactionVolume())
                    )
            );
            position.setPositionQuantity(quantity);
            position.setPositionCost(cost);
            positionMapper.updatePosition(position);
            positionMapper.updatePositionStatus(position.getPositionId(), "active");
        }
        taskMapper.updateTaskStatusById(task.getTaskId(), "filled");
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    private boolean sellTransaction(TransactionTask task) {
        int defaultBid = accountMapper.selectDefaultBrokerageByUserId(task.getUserId());
        BigDecimal balance = brokerageAccountMapper.selectBrokerageBalance(task.getUserId(), defaultBid);
        BigDecimal income = task.getTransactionPrice().multiply(new BigDecimal(task.getTransactionVolume()));
        brokerageAccountMapper.updateBrokerageBalance(task.getUserId(), defaultBid, balance.add(income));
        taskMapper.updateTaskStatusById(task.getTaskId(), "filled");
        return true;
    }

    private static boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 判断是否为周六或周日
        return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
    }

    private static boolean isAStockTradingTime() {
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();

        // 获取当前时间
        LocalTime currentTime = now.toLocalTime();

        // 获取当前日期
        LocalDate currentDate = now.toLocalDate();

        // 判断是否为工作日
        if (!isWorkingDay(currentDate)) {
            return false;
        }

        // 判断是否为A股可交易时间（假设A股交易时间为上午 9:30 到下午 15:00）
        LocalTime aStockStartTime1 = LocalTime.of(9, 30);
        LocalTime aStockEndTime1 = LocalTime.of(11, 30);
        LocalTime aStockStartTime2 = LocalTime.of(13, 0);
        LocalTime aStockEndTime2 = LocalTime.of(15, 0);

        return (currentTime.isAfter(aStockStartTime1) && currentTime.isBefore(aStockEndTime1)) ||
                (currentTime.isAfter(aStockStartTime2) && currentTime.isBefore(aStockEndTime2));
    }
}
