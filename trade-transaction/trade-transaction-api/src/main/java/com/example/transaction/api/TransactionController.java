package com.example.transaction.api;

import com.example.transaction.config.common.ErrorMessage;
import com.example.transaction.config.common.SuccessMessage;
import com.example.transaction.service.AuthService;
import com.example.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    AuthService authService;

    @PostMapping("/r/buy")
    public Object buy(@RequestParam("code") String code,
                      @RequestParam("price") BigDecimal price,
                      @RequestParam("volume") Integer volume) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
//        else if (code == null || price == null || volume == null)
//            return new ErrorMessage("参数缺失，请至少键入证券代码、买入价格、买入量").getMessage();
        try {
            if (volume % 100 != 0 || volume <= 0) return new ErrorMessage("买入量至少为一手，即100股").getMessage();
            int state = transactionService.buy(uid, code, price, volume);
            if (state == -1) return new ErrorMessage("您的账户余额不足").getMessage();
            else if (state == 1) return new SuccessMessage("委托买入" + code + "成功").getMessage();
            else return new ErrorMessage("委托买入" + code + "失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("委托买入" + code + "失败").getMessage();
        }
    }

    @PostMapping("/r/sell")
    public Object sellCurrency(@RequestParam("code") String code,
                               @RequestParam("price") BigDecimal price,
                               @RequestParam("volume") Integer volume) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
//        else if (code == null || price == null || volume == null)
//            return new ErrorMessage("参数缺失，请至少键入证券代码、卖出价格、卖出量").getMessage();
        try {
            if (volume % 100 != 0 || volume <= 0) return new ErrorMessage("卖出量至少为一手，即100股").getMessage();
            int state = transactionService.sell(uid, code, price, volume);
            if (state == -1) return new ErrorMessage("您的" + code + "持仓量不足").getMessage();
            else if (state == 1) return new SuccessMessage("委托卖出" + code + "成功").getMessage();
            else return new ErrorMessage("委托卖出" + code + "失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("委托卖出" + code + "失败").getMessage();
        }
    }

    @PostMapping("/r/cancel")
    public Object cancelTransaction(@RequestParam("tid") Integer tid) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        try {
            String taskType = transactionService.getTaskType(tid);
            if (taskType.equals("buy")) {
                int state = transactionService.buyCancel(tid);
                if (state == -1) return new ErrorMessage("只能撤单待交易的订单").getMessage();
                else return new SuccessMessage("买入交易撤单成功", true).getMessage();
            } else if (taskType.equals("sell")) {
                int state = transactionService.sellCancel(tid);
                if (state == -1) return new ErrorMessage("只能撤单待交易的订单").getMessage();
                else return new SuccessMessage("卖出交易撤单成功", true).getMessage();
            }
            else return new ErrorMessage("交易撤单失败").getMessage();
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("交易撤单失败").getMessage();
        }
    }

    @GetMapping("/r/position")
    public Object getPosition() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        try {
            return new SuccessMessage<>("获取持仓情况成功", transactionService.getActivePositions(uid)).getMessage();
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("获取持仓情况失败").getMessage();
        }

    }

    @PostMapping("/r/details")
    public Object getAllTradeDetails(@RequestParam(value = "status", required = false) String status) throws InterruptedException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        else if (status == null) return new SuccessMessage<>("获取交易明细成功",
                transactionService.getAllTasks(uid)).getMessage();
        else if (!status.equals("active") && !status.equals("filled") && !status.equals("cancelled"))
            return new ErrorMessage("交易明细状态只能为active、filled或cancelled").getMessage();
        try {
            return new SuccessMessage<>("获取" + status + "交易明细成功",
                    transactionService.getTasksByStatus(uid, status)).getMessage();
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("获取交易明细失败").getMessage();
        }
    }
}
