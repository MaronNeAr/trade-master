package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class TransactionMarket {
    private Integer marketId;
    private String name;
    private String code;
    private BigDecimal lastPrice;  // 使用 BigDecimal 表示金融数据，以确保精度
    private BigDecimal previousClose;
    private BigDecimal open;
    private BigDecimal volumeHands;
    private BigDecimal outsideDisc;
    private BigDecimal insideDisc;
    private BigDecimal buy1, buy2, buy3, buy4, buy5;  // 买单价格
    private BigDecimal buy1Volume, buy2Volume, buy3Volume, buy4Volume, buy5Volume;  // 买单数量
    private BigDecimal sell1, sell2, sell3, sell4, sell5;  // 卖单价格
    private BigDecimal sell1Volume, sell2Volume, sell3Volume, sell4Volume, sell5Volume;  // 卖单数量
    private String recentTransaction;
    private String time;  // 时间可以考虑使用 java.time 类型
    private BigDecimal change;  // 涨跌额
    private BigDecimal changePercent;  // 涨跌百分比
    private BigDecimal high;
    private BigDecimal low;
    private String priceVolumeTurnover;  // 价格/成交量/成交额可能需要拆分为不同字段
    private BigDecimal volumeHands2;
    private BigDecimal turnoverTenThousand;  // 成交额（万）
    private BigDecimal turnoverRate;  // 换手率
    private BigDecimal peRatio;  // 市盈率
    private String empty;  // 此字段的含义可能需要更多上下文
    private BigDecimal high2;
    private BigDecimal low2;
    private BigDecimal amplitude;  // 振幅
    private BigDecimal circulationMarketValue;  // 流通市值
    private BigDecimal totalMarketValue;  // 总市值
    private BigDecimal pbRatio;  // 市净率
    private BigDecimal limitUpPrice;  // 涨停价
    private BigDecimal limitDownPrice;  // 跌停价
    private Timestamp createTime;

    public TransactionMarket(Integer marketId, String name, String code, BigDecimal lastPrice, BigDecimal previousClose, BigDecimal open, BigDecimal volumeHands, BigDecimal outsideDisc, BigDecimal insideDisc, BigDecimal buy1, BigDecimal buy2, BigDecimal buy3, BigDecimal buy4, BigDecimal buy5, BigDecimal buy1Volume, BigDecimal buy2Volume, BigDecimal buy3Volume, BigDecimal buy4Volume, BigDecimal buy5Volume, BigDecimal sell1, BigDecimal sell2, BigDecimal sell3, BigDecimal sell4, BigDecimal sell5, BigDecimal sell1Volume, BigDecimal sell2Volume, BigDecimal sell3Volume, BigDecimal sell4Volume, BigDecimal sell5Volume, String recentTransaction, String time, BigDecimal change, BigDecimal changePercent, BigDecimal high, BigDecimal low, String priceVolumeTurnover, BigDecimal volumeHands2, BigDecimal turnoverTenThousand, BigDecimal turnoverRate, BigDecimal peRatio, String empty, BigDecimal high2, BigDecimal low2, BigDecimal amplitude, BigDecimal circulationMarketValue, BigDecimal totalMarketValue, BigDecimal pbRatio, BigDecimal limitUpPrice, BigDecimal limitDownPrice, Timestamp createTime) {
        this.marketId = marketId;
        this.name = name;
        this.code = code;
        this.lastPrice = lastPrice;
        this.previousClose = previousClose;
        this.open = open;
        this.volumeHands = volumeHands;
        this.outsideDisc = outsideDisc;
        this.insideDisc = insideDisc;
        this.buy1 = buy1;
        this.buy2 = buy2;
        this.buy3 = buy3;
        this.buy4 = buy4;
        this.buy5 = buy5;
        this.buy1Volume = buy1Volume;
        this.buy2Volume = buy2Volume;
        this.buy3Volume = buy3Volume;
        this.buy4Volume = buy4Volume;
        this.buy5Volume = buy5Volume;
        this.sell1 = sell1;
        this.sell2 = sell2;
        this.sell3 = sell3;
        this.sell4 = sell4;
        this.sell5 = sell5;
        this.sell1Volume = sell1Volume;
        this.sell2Volume = sell2Volume;
        this.sell3Volume = sell3Volume;
        this.sell4Volume = sell4Volume;
        this.sell5Volume = sell5Volume;
        this.recentTransaction = recentTransaction;
        this.time = time;
        this.change = change;
        this.changePercent = changePercent;
        this.high = high;
        this.low = low;
        this.priceVolumeTurnover = priceVolumeTurnover;
        this.volumeHands2 = volumeHands2;
        this.turnoverTenThousand = turnoverTenThousand;
        this.turnoverRate = turnoverRate;
        this.peRatio = peRatio;
        this.empty = empty;
        this.high2 = high2;
        this.low2 = low2;
        this.amplitude = amplitude;
        this.circulationMarketValue = circulationMarketValue;
        this.totalMarketValue = totalMarketValue;
        this.pbRatio = pbRatio;
        this.limitUpPrice = limitUpPrice;
        this.limitDownPrice = limitDownPrice;
        this.createTime = createTime;
    }

    public TransactionMarket() {
    }

    public TransactionMarket(StockQuote stock, Timestamp createTime) {
        this.name = stock.getName();
        this.code = stock.getCode();
        this.lastPrice = stock.getLastPrice();
        this.previousClose = stock.getPreviousClose();
        this.open = stock.getOpen();
        this.volumeHands = stock.getVolumeHands();
        this.outsideDisc = stock.getOutsideDisc();
        this.insideDisc = stock.getInsideDisc();
        this.buy1 = stock.getBuy1();
        this.buy2 = stock.getBuy2();
        this.buy3 = stock.getBuy3();
        this.buy4 = stock.getBuy4();
        this.buy5 = stock.getBuy5();
        this.buy1Volume = stock.getBuy1Volume();
        this.buy2Volume = stock.getBuy2Volume();
        this.buy3Volume = stock.getBuy3Volume();
        this.buy4Volume = stock.getBuy4Volume();
        this.buy5Volume = stock.getBuy5Volume();
        this.sell1 = stock.getSell1();
        this.sell2 = stock.getSell2();
        this.sell3 = stock.getSell3();
        this.sell4 = stock.getSell4();
        this.sell5 = stock.getSell5();
        this.sell1Volume = stock.getSell1Volume();
        this.sell2Volume = stock.getSell2Volume();
        this.sell3Volume = stock.getSell3Volume();
        this.sell4Volume = stock.getSell4Volume();
        this.sell5Volume = stock.getSell5Volume();
        this.recentTransaction = stock.getRecentTransaction();
        this.time = stock.getTime();
        this.change = stock.getChange();
        this.changePercent = stock.getChangePercent();
        this.high = stock.getHigh();
        this.low = stock.getLow();
        this.priceVolumeTurnover = stock.getPriceVolumeTurnover();
        this.volumeHands2 = stock.getVolumeHands2();
        this.turnoverTenThousand = stock.getTurnoverTenThousand();
        this.turnoverRate = stock.getTurnoverRate();
        this.peRatio = stock.getPeRatio();
        this.empty = stock.getEmpty();
        this.high2 = stock.getHigh2();
        this.low2 = stock.getLow2();
        this.amplitude = stock.getAmplitude();
        this.circulationMarketValue = stock.getCirculationMarketValue();
        this.totalMarketValue = stock.getTotalMarketValue();
        this.pbRatio = stock.getPbRatio();
        this.limitUpPrice = stock.getLimitUpPrice();
        this.limitDownPrice = stock.getLimitDownPrice();
        this.createTime = createTime;
    }
}


