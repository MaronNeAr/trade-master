package com.example.transaction.service;

import com.example.transaction.model.po.StockPriceSeries;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionMarket;
import com.example.transaction.model.po.TransactionSecurity;
import com.example.transaction.model.vo.QuantMarketVo;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

public interface MarketService {
    StockQuote getMarketData(String code) throws UnsupportedEncodingException;

    List<StockPriceSeries> getHistoryData(String code);

    List<TransactionSecurity> getSSE50Security();

    List<TransactionSecurity> queryByType(String type, String exchange, String keyword);

    List<TransactionSecurity> queryBySubtype(String subtype, String exchange, String keyword);

    List<QuantMarketVo> getQuantMarketByCode(String code);

    List<QuantMarketVo> getQuantMarketByCodeAndDate(String code, String startDate, String endDate) throws ParseException;
}
