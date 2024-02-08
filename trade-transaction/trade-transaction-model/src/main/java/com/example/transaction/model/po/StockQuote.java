package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockQuote {
    private String unknown;
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

    public StockQuote() {
    }

    public StockQuote(String unknown, String name, String code, String lastPrice, String previousClose, String open, String volumeHands, String outsideDisc, String insideDisc, String buy1, String buy2, String buy3, String buy4, String buy5, String buy1Volume, String buy2Volume, String buy3Volume, String buy4Volume, String buy5Volume, String sell1, String sell2, String sell3, String sell4, String sell5, String sell1Volume, String sell2Volume, String sell3Volume, String sell4Volume, String sell5Volume, String recentTransaction, String time, String change, String changePercent, String high, String low, String priceVolumeTurnover, String volumeHands2, String turnoverTenThousand, String turnoverRate, String peRatio, String empty, String high2, String low2, String amplitude, String circulationMarketValue, String totalMarketValue, String pbRatio, String limitUpPrice, String limitDownPrice) {
        this.unknown = unknown;
        this.name = name;
        this.code = code;
        this.lastPrice = new BigDecimal(lastPrice);
        this.previousClose = new BigDecimal(previousClose);
        this.open = new BigDecimal(open);
        this.volumeHands = new BigDecimal(volumeHands);
        this.outsideDisc = new BigDecimal(outsideDisc);
        this.insideDisc = new BigDecimal(insideDisc);
        this.buy1 = new BigDecimal(buy1);
        this.buy2 = new BigDecimal(buy2);
        this.buy3 = new BigDecimal(buy3);
        this.buy4 = new BigDecimal(buy4);
        this.buy5 = new BigDecimal(buy5);
        this.buy1Volume = new BigDecimal(buy1Volume);
        this.buy2Volume = new BigDecimal(buy2Volume);
        this.buy3Volume = new BigDecimal(buy3Volume);
        this.buy4Volume = new BigDecimal(buy4Volume);
        this.buy5Volume = new BigDecimal(buy5Volume);
        this.sell1 = new BigDecimal(sell1);
        this.sell2 = new BigDecimal(sell2);
        this.sell3 = new BigDecimal(sell3);
        this.sell4 = new BigDecimal(sell4);
        this.sell5 = new BigDecimal(sell5);
        this.sell1Volume = new BigDecimal(sell1Volume);
        this.sell2Volume = new BigDecimal(sell2Volume);
        this.sell3Volume = new BigDecimal(sell3Volume);
        this.sell4Volume = new BigDecimal(sell4Volume);
        this.sell5Volume = new BigDecimal(sell5Volume);
        this.recentTransaction = recentTransaction;
        this.time = time;
        this.change = new BigDecimal(change);
        this.changePercent = new BigDecimal(changePercent);
        this.high = new BigDecimal(high);
        this.low = new BigDecimal(low);
        this.priceVolumeTurnover = priceVolumeTurnover;
        this.volumeHands2 = new BigDecimal(volumeHands2);
        this.turnoverTenThousand = new BigDecimal(turnoverTenThousand);
        this.turnoverRate = new BigDecimal(turnoverRate);
        this.peRatio = new BigDecimal(peRatio);
        this.empty = empty;
        this.high2 = new BigDecimal(high2);
        this.low2 = new BigDecimal(low2);
        this.amplitude = new BigDecimal(amplitude);
        this.circulationMarketValue = new BigDecimal(circulationMarketValue);
        this.totalMarketValue = new BigDecimal(totalMarketValue);
        this.pbRatio = new BigDecimal(pbRatio);
        this.limitUpPrice = new BigDecimal(limitUpPrice);
        this.limitDownPrice = new BigDecimal(limitDownPrice);
    }

    public StockQuote(String unknown, String name, String code, BigDecimal lastPrice, BigDecimal previousClose, BigDecimal open, BigDecimal volumeHands, BigDecimal outsideDisc, BigDecimal insideDisc, BigDecimal buy1, BigDecimal buy2, BigDecimal buy3, BigDecimal buy4, BigDecimal buy5, BigDecimal buy1Volume, BigDecimal buy2Volume, BigDecimal buy3Volume, BigDecimal buy4Volume, BigDecimal buy5Volume, BigDecimal sell1, BigDecimal sell2, BigDecimal sell3, BigDecimal sell4, BigDecimal sell5, BigDecimal sell1Volume, BigDecimal sell2Volume, BigDecimal sell3Volume, BigDecimal sell4Volume, BigDecimal sell5Volume, String recentTransaction, String time, BigDecimal change, BigDecimal changePercent, BigDecimal high, BigDecimal low, String priceVolumeTurnover, BigDecimal volumeHands2, BigDecimal turnoverTenThousand, BigDecimal turnoverRate, BigDecimal peRatio, String empty, BigDecimal high2, BigDecimal low2, BigDecimal amplitude, BigDecimal circulationMarketValue, BigDecimal totalMarketValue, BigDecimal pbRatio, BigDecimal limitUpPrice, BigDecimal limitDownPrice) {
        this.unknown = unknown;
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
    }
}
