package com.example.transaction.service.impl;

import com.example.base.utils.HttpClient;
import com.example.transaction.mapper.TransactionMarketMapper;
import com.example.transaction.mapper.TransactionSecurityMapper;
import com.example.transaction.model.po.StockPriceSeries;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionMarket;
import com.example.transaction.model.po.TransactionSecurity;
import com.example.transaction.service.MarketService;
import com.example.transaction.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MarketServiceImpl implements MarketService {
    @Autowired
    TransactionMarketMapper marketMapper;

    @Autowired
    TransactionSecurityMapper securityMapper;

    @Autowired
    RedisService redisService;

    @Override
    public StockQuote getMarketData(String code) throws UnsupportedEncodingException {
        if (redisService.hasKey("security/" + code)) return (StockQuote) redisService.get("security/" + code);

        String response = HttpClient.sendGetRequest( "https://qt.gtimg.cn/q=" + code, "GBK");
        if (response.length() < 100) return null;
        String[] params = response.substring(12, response.length() - 2).split("~");
        if (params.length < 48) return null;
        for (int i = 0; i < params.length; i++) {
            if (params[i].equals("")) params[i] = "0";
        }
        StockQuote stock = new StockQuote(
                params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8],
                params[9], params[11], params[13], params[15], params[17], params[10], params[12], params[14], params[16], params[18], params[19],
                params[21], params[23], params[25], params[27], params[20], params[22], params[24], params[26], params[28], params[29],
                params[30], params[31], params[32], params[33], params[34], params[35], params[36], params[37], params[38], params[39],
                params[40], params[41], params[42], params[43], params[44], params[45], params[46], params[47], params[48]
        );
        redisService.set("security/" + code, stock, 3);
        return stock;
    }

    @Override
    public List<StockPriceSeries> getHistoryData(String code) {
        if (redisService.hasKey("series/week/" + code)) return (List<StockPriceSeries>) redisService.get("series/week/" + code);

        String response = HttpClient.sendGetRequest("https://data.gtimg.cn/flashdata/hushen/weekly/" + code + ".js");
        if (response.length() < 100) return null;

        String[] formData = response.substring(16, response.length() - 5).split("\\\\n\\\\");
        List<StockPriceSeries> series = new ArrayList<>();

        for (String row: formData) {
            String[] rowData = row.split(" ");
//            System.out.println(Arrays.toString(rowData));
            series.add(new StockPriceSeries(rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], rowData[5]));
        }

        redisService.set("series/week/" + code, series, 24 * 3600);
        return series;
    }

    @Override
    public List<TransactionSecurity> queryByType(String type, String exchange, String keyword) {
        return securityMapper.selectSecurityByTypeAndExchange(type, exchange, keyword);
    }

    @Override
    public List<TransactionSecurity> queryBySubtype(String subtype, String exchange, String keyword) {
        return securityMapper.selectSecurityBySubtypeAndExchange(subtype, exchange, keyword);
    }

    @Override
    public List<TransactionMarket> getQuantMarketByCode(String code) {
        if (redisService.hasKey("series/quant/" + code)) return (List<TransactionMarket>) redisService.get("series/quant/" + code);

        List<TransactionMarket> series = marketMapper.selectMarketByCode(code);
        redisService.set("series/quant/" + code, series, 24 * 3600);

        return series;
    }

    @Override
    public List<TransactionMarket> getQuantMarketByCodeAndDate(String code, String startDate, String endDate) throws ParseException {
        if (redisService.hasKey("series/quant/" + code + "/" + startDate + "-" + endDate)) return (List<TransactionMarket>) redisService.get("series/quant/" + code + "/" + startDate + "-" + endDate);

        List<TransactionMarket> series = marketMapper.selectMarketByCodeAndDate(code, startDate, endDate);
        redisService.set("series/quant/" + code + "/" + startDate + "-" + endDate, series, 24 * 3600);

        return series;
    }
}
