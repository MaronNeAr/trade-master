package com.example.transaction.api;

import com.example.transaction.config.common.ErrorMessage;
import com.example.transaction.config.common.SuccessMessage;
import com.example.transaction.model.po.StockPriceSeries;
import com.example.transaction.model.po.StockQuote;
import com.example.transaction.model.po.TransactionSecurity;
import com.example.transaction.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    public Object querySecurityGroup(HttpServletRequest req) {
        try {
            String type = req.getParameter("type");
            String exchange = req.getParameter("exchange");
            String keyword = req.getParameter("keyword");
            if (type == null || exchange == null) return new ErrorMessage("请至少键入type和exchange两个参数以查询行情列表").getMessage();
            else if (typeSet.contains(type))
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
}
