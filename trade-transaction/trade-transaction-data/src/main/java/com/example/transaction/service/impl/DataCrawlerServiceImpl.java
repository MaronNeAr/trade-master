package com.example.transaction.service.impl;

import com.example.transaction.mapper.TransactionMarketMapper;
import com.example.transaction.mapper.TransactionSecurityMapper;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionMarket;
import com.example.transaction.model.po.TransactionSecurity;
import com.example.transaction.service.DataCrawlerService;
import com.example.transaction.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DataCrawlerServiceImpl implements DataCrawlerService {

    @Autowired
    MarketService marketService;

    @Autowired
    TransactionMarketMapper marketMapper;

    @Autowired
    TransactionSecurityMapper securityMapper;

    @Override
    @Scheduled(fixedRate = 60000)
    public int genSecurityData() {
        if (!isAStockTradingTime()) return -1;
        List<TransactionSecurity> securityList = securityMapper.selectSSE50Security();
        securityList.forEach((item) -> {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp + ": 正在添加" + item.getSecurityName() + "[" + item.getSecurityCode() + "]" +  "的行情数据");
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        StockQuote stock = marketService.getMarketData(item.getSecurityCode());
                        System.out.println(stock);
                        marketMapper.insertMarket(new TransactionMarket(stock, timestamp));
                    } catch (UnsupportedEncodingException e) {
                        System.out.println(e);
                    }
                }
            }).start();
        });
        return 1;
//        StockQuote stock = marketService.getMarketData();
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
