package com.example.transaction.api;

import com.example.transaction.config.common.ErrorMessage;
import com.example.transaction.config.common.SuccessMessage;
import com.example.transaction.model.po.TransactionBrokerage;
import com.example.transaction.service.AccountService;
import com.example.transaction.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    AuthService authService;

    @GetMapping("/r/info")
    public String getUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return authService.getUidByPrincipal(principal);
    }

    @GetMapping("/brokerages")
    public Object getBrokerages() {
        try {
            return new SuccessMessage<List<TransactionBrokerage>> ("获取证券委托商信息成功", accountService.getBrokerageList()).getMessage();
        } catch (Exception e) {
            System.out.println(e);
            return new ErrorMessage("获取证券委托商信息失败").getMessage();
        }
    }

    @GetMapping("/r/balance")
    public Object getBalance() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        try {
            return new SuccessMessage<BigDecimal>("获取个人账户余额成功", accountService.getBalance(uid)).getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("获取个人账户余额失败").getMessage();
        }
    }

    @PostMapping("/r/transfer")
    public Object transfer(HttpServletRequest req) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        String bid = req.getParameter("bid");
        String amount = req.getParameter("amount");
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        else if (bid == null || amount == null) return new ErrorMessage("参数缺失，请至少键入证券委托商、转账额度").getMessage();
        try {
            int state = accountService.transfer(uid, Integer.valueOf(bid), new BigDecimal(amount));
            if (state == -1) return new ErrorMessage("您的个人账户余额不足").getMessage();
            else if (state == 1) return new SuccessMessage("证券账户充值￥" + amount + "成功").getMessage();
            else return new ErrorMessage("证券账户充值失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("证券账户充值失败").getMessage();
        }
    }

    @PostMapping("/r/withdraw")
    public Object withdraw(HttpServletRequest req) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        String bid = req.getParameter("bid");
        String amount = req.getParameter("amount");
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        else if (bid == null || amount == null) return new ErrorMessage("参数缺失，请至少键入证券委托商、转账额度").getMessage();
        try {
            int state = accountService.withdraw(uid, Integer.valueOf(bid), new BigDecimal(amount));
            if (state == -1) return new ErrorMessage("您的证券账户余额不足").getMessage();
            else if (state == 1) return new SuccessMessage("证券账户体现￥" + amount + "成功").getMessage();
            else return new ErrorMessage("证券账户提现失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("证券账户提现失败").getMessage();
        }
    }
}
