package com.example.messagesdk;

import com.example.messagesdk.service.MqMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2022/9/21 21:51
 */
@SpringBootTest
public class MessageProcessClassTest {

    @Autowired
    MessageProcessClass messageProcessClass;

    @Autowired
    MqMessageService mqMessageService;

    @Test
    public void test() {
        System.out.println("开始执行-----》" + LocalDateTime.now());
        messageProcessClass.process(0, 1, "buy", 2, 10);
        System.out.println("结束执行-----》" + LocalDateTime.now());
    }

    @Test
    public void test01() {
        System.out.println("添加消息" + LocalDateTime.now());
        mqMessageService.addMessage("buy", "marlon1475", "sh600000/6.53/100", String.valueOf(LocalDateTime.now()));
        System.out.println("添加成功" + LocalDateTime.now());
    }
}
