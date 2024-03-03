package com.example.transaction.service;

import com.example.transaction.model.po.StockPriceSeries;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionMarket;
import com.example.transaction.model.po.TransactionSecurity;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

public interface MarketService {
    StockQuote getMarketData(String code) throws UnsupportedEncodingException;

    List<StockPriceSeries> getHistoryData(String code);

    List<TransactionSecurity> queryByType(String type, String exchange, String keyword);

    List<TransactionSecurity> queryBySubtype(String subtype, String exchange, String keyword);

    List<TransactionMarket> getQuantMarketByCode(String code);

    List<TransactionMarket> getQuantMarketByCodeAndDate(String code, String startDate, String endDate) throws ParseException;
}
