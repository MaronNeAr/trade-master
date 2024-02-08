package com.example;

import com.example.transaction.TransactionApiApplication;
import com.example.transaction.mapper.TransactionTaskMapper;
import com.example.transaction.model.po.TransactionTask;
import com.example.transaction.service.ExecutionService;
import com.example.transaction.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApiApplication.class)
public class TransactionExecutionTests {
    @Autowired
    TransactionService transactionService;

    @Autowired
    ExecutionService executionService;

    @Autowired
    TransactionTaskMapper taskMapper;

    @Test
    public void test01() throws JsonProcessingException {
//        int state = transactionService.sell(new TransactionTask(1,1, "sh600004", new Timestamp(System.currentTimeMillis()), "sell", new BigDecimal(9.73), 100, "active"));
//        System.out.println(state);
    }

    @Test
    public void test02() {
        List<TransactionTask> tasks1 = taskMapper.selectTaskListByUserId(1);
        System.out.println(tasks1);
        List<TransactionTask> tasks2 = taskMapper.selectTaskListByStatusAndType("active", "buy");
        System.out.println(tasks2);
        taskMapper.updateTaskStatusById(1, "active");
    }

    @Test
    public void test03() throws UnsupportedEncodingException, JsonProcessingException {
        transactionService.buyCancel(4);
    }
}
