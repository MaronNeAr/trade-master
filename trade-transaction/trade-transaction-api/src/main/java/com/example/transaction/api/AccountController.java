package com.example.transaction.api;

import com.example.transaction.config.common.ErrorMessage;
import com.example.transaction.config.common.SuccessMessage;
import com.example.transaction.model.dto.TransactionBrokerageAccountDTO;
import com.example.transaction.model.po.TransactionBrokerage;
import com.example.transaction.model.po.TransactionBrokerageAccount;
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
            return new SuccessMessage<BigDecimal>("获取个人账户余额成功", accountService.getAccountBalance(uid)).getMessage();
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
            if (Integer.valueOf(amount) <= 0) return new ErrorMessage("转账额度为正值").getMessage();
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
            if (Integer.valueOf(amount) <= 0) return new ErrorMessage("转账额度为正值").getMessage();
            int state = accountService.withdraw(uid, Integer.valueOf(bid), new BigDecimal(amount));
            if (state == -1) return new ErrorMessage("您的证券账户余额不足").getMessage();
            else if (state == 1) return new SuccessMessage("证券账户提现￥" + amount + "成功").getMessage();
            else return new ErrorMessage("证券账户提现失败").getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("证券账户提现失败").getMessage();
        }
    }

    @GetMapping("/r/brokerage_account")
    public Object getBrokerageAccount() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        try {
            return new SuccessMessage<List<TransactionBrokerageAccountDTO>>("获取证券账户成功", accountService.getBrokerageAccountByUserId(uid)).getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("获取证券账户失败").getMessage();
        }
    }

    @GetMapping("/r/default_brokerage_id")
    public Object getDefaultBrokerageId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String uid = authService.getUidByPrincipal(principal);
        if (uid == null) return new ErrorMessage("用户认证失败，请重新登录");
        try {
            int ans = accountService.getDefaultBrokerage(uid);
            if (ans == -1) return new ErrorMessage("尚未配置默认证券账户").getMessage();
            else return new SuccessMessage<>("获取默认证券账户ID成功", ans).getMessage();
        } catch(Exception e) {
            System.out.println(e);
            return new ErrorMessage("获取默认证券ID失败").getMessage();
        }
    }
}
