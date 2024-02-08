package com.example.transaction.model.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StockPriceSeries {
    private String date;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal low;
    private Integer volume;

    public StockPriceSeries() {
    }

    public StockPriceSeries(String date, String open, String close, String high, String low, String volume) {
        this.date = date;
        this.open = new BigDecimal(open);
        this.close = new BigDecimal(close);
        this.high = new BigDecimal(high);
        this.low = new BigDecimal(low);
        this.volume = Integer.valueOf(volume);
    }

    public StockPriceSeries(String date, BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low, Integer volume) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
    }
}
