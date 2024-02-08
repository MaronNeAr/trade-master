package com.example.transaction.api;

import com.example.transaction.config.common.ErrorMessage;
import com.example.transaction.config.common.SuccessMessage;
import com.example.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping("/buy")
    public Object buy(HttpServletRequest req) {
        String code = req.getParameter("code");
        String price = req.getParameter("price");
        String volume = req.getParameter("volume");
        if (code == null || price == null || volume == null) return new ErrorMessage("参数缺失，请至少键入证券代码、买入价格、买入量").getMessage();
        try {
            int uid = 1;
            if (Integer.valueOf(volume) % 100 != 0) return new ErrorMessage("买入量至少为一手，即100股").getMessage();
            int state = transactionService.buy(uid, code, new BigDecimal(Integer.valueOf(price)), Integer.valueOf(volume));
            if (state == -1) return new ErrorMessage("您的账户余额不足").getMessage();
            else if (state == 1) return new SuccessMessage("委托买入" + code + "成功").getMessage();
            else return new ErrorMessage("委托买入" + code + "失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("委托买入" + code + "失败").getMessage();
        }
    }

    @PostMapping("/sell")
    public Object sellCurrency(HttpServletRequest req) {
        String code = req.getParameter("code");
        String price = req.getParameter("price");
        String volume = req.getParameter("volume");
        if (code == null || price == null || volume == null) return new ErrorMessage("参数缺失，请至少键入证券代码、卖出价格、卖出量").getMessage();
        try {
            int uid = 1;
            if (Integer.valueOf(volume) % 100 != 0) return new ErrorMessage("卖出量至少为一手，即100股").getMessage();
            int state = transactionService.sell(uid, code, new BigDecimal(Integer.valueOf(price)), Integer.valueOf(volume));
            if (state == -1) return new ErrorMessage("您的" + code + "持仓量不足").getMessage();
            else if (state == 1) return new SuccessMessage("委托卖出" + code + "成功").getMessage();
            else return new ErrorMessage("委托卖出" + code + "失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("委托卖出" + code + "失败").getMessage();
        }
    }

    @PostMapping("/position")
    public Object getPosition(HttpServletRequest req) {
        return null;
    }

    @GetMapping("/details")
    public Object getAllTradeDetail() {
        return null;
    }

    @PostMapping("/detail")
    public Object getTradeDetails(HttpServletRequest req) {
        return null;
    }
}
