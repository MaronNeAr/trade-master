package com.example;

import com.example.transaction.model.po.TransactionRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;

@SpringBootTest
public class TransactionUnitApplicationTests {

    @Test
    public void Test01() {
        BigDecimal decimal = new BigDecimal("0.00");
        System.out.println(decimal);
    }

    @Test
    public void Test02() throws IOException {
        TransactionRecord record = new TransactionRecord(2, 1, "sh600000", new Timestamp(100432423), "buy",new BigDecimal(6.54), 100);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(record);
        TransactionRecord obj = objectMapper.readValue(jsonString, TransactionRecord.class);
        System.out.println(obj);
    }
}
