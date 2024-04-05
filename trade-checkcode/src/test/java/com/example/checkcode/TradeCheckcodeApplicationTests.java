package com.example.checkcode;

import com.example.checkcode.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

@SpringBootTest
class TradeCheckcodeApplicationTests {

    @Test
    void contextLoads() throws MessagingException {
        MailUtil.sendTestMail("marlon1475@foxmail.com", "1234");
    }

}
