package com.example;

import com.example.transaction.TransactionApiApplication;
import com.example.transaction.mapper.TransactionAccountMapper;
import com.example.transaction.mapper.TransactionBrokerageAccountMapper;
import com.example.transaction.mapper.TransactionBrokerageMapper;
import com.example.transaction.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApiApplication.class)
public class TransactionAccountTests {
    @Autowired
    TransactionBrokerageMapper brokerageMapper;

    @Autowired
    TransactionBrokerageAccountMapper brokerageAccountMapper;

    @Autowired
    TransactionAccountMapper accountMapper;

    @Autowired
    AccountService accountService;

    @Test
    public void test01() {
//        System.out.println(brokerageMapper.selectBrokerageList());
        int bid = accountMapper.selectDefaultBrokerageByUserId("1");
        System.out.println(brokerageAccountMapper.selectBrokerageAccountByUserId("1"));
        System.out.println(brokerageAccountMapper.selectBrokerageBalance("1", bid));
    }

    @Test
    public void test02() {
        int state = accountService.withdraw("1", 119, new BigDecimal(22));
        System.out.println(state);
    }
}
