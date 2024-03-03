package com.example.transaction.api;

import com.example.transaction.config.common.ErrorMessage;
import com.example.transaction.config.common.SuccessMessage;
import com.example.transaction.model.po.StockPriceSeries;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionSecurity;
import com.example.transaction.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
public class MarketController {
    @Autowired
    MarketService marketService;

    private static Set<String> typeSet = new HashSet<String>(Arrays.asList("STOCK", "FUND", "BOND", "INDEX", "FUTURES"));

    @GetMapping("/query/{code}")
    public Object marketQuery(@PathVariable String code) {
        try {
            StockQuote result = marketService.getMarketData(code);
            if (result == null) return new ErrorMessage("查询" + code + "行情数据失败").getMessage();
            else return new SuccessMessage<StockQuote>("查询" + code + "行情数据成功", result).getMessage();
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("查询" + code + "行情数据失败").getMessage();
        }

    }

    @GetMapping("/history/{code}")
    public Object historyMarketQuery(@PathVariable String code) {
        try {
            List<StockPriceSeries> result = marketService.getHistoryData(code);
            if (result == null) return new ErrorMessage("查询" + code + "历史数据失败").getMessage();
            else return new SuccessMessage<List<StockPriceSeries>>("查询" + code + "历史数据成功", result).getMessage();
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("查询" + code + "历史数据失败").getMessage();
        }

    }

    @PostMapping("query/securities")
    public Object querySecurityGroup(@RequestParam("type") String type,
                                     @RequestParam("exchange") String exchange,
                                     @RequestParam(value = "keyword", required = false) String keyword) {
        try {
            if (typeSet.contains(type))
                return new SuccessMessage<List<TransactionSecurity>>(
                    "查询行情列表成功",
                    marketService.queryByType(type, exchange, keyword)
                ).getMessage();
            else {
                return new SuccessMessage<List<TransactionSecurity>>(
                        "查询行情列表成功",
                        marketService.queryBySubtype(type, exchange, keyword)
                ).getMessage();
            }
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("查询行情列表成失败").getMessage();
        }
    }

    @PostMapping("market")
    public Object getQuantMarketByCode(@RequestParam("code") String code,
                                  @RequestParam(value = "startDate", required = false) String startDate,
                                  @RequestParam(value = "endDate", required = false) String endDate) {
        try {
            if (startDate == null || endDate == null) return new SuccessMessage<>("查询量化行情成功", marketService.getQuantMarketByCode(code)).getMessage();
            else return new SuccessMessage<>("查询量化行情成功", marketService.getQuantMarketByCodeAndDate(code, startDate, endDate)).getMessage();
        } catch (ParseException e) {
            System.out.println(e);
            return new ErrorMessage("查询量化行情失败").getMessage();
        }
    }

}
