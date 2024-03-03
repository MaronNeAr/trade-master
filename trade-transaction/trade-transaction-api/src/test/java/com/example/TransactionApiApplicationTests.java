package com.example;

import com.example.base.utils.HttpClient;
import com.example.messagesdk.service.MqMessageService;
import com.example.transaction.TransactionApiApplication;
import com.example.transaction.model.po.StockPriceSeries;
import com.example.transaction.model.po.TransactionAccount;
import com.example.transaction.model.po.TransactionMarket;
import com.example.transaction.model.po.TransactionSecurity;
import com.example.transaction.service.MarketService;
import com.example.transaction.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApiApplication.class)
//@SpringBootTest
public class TransactionApiApplicationTests {

    @Autowired
    private MarketService marketService;

    @Autowired
    private MqMessageService mqMessageService;

    @Autowired
    private RedisService redisService;

    @Test
    public void Test01() throws UnsupportedEncodingException {
        String response = HttpClient.sendGetRequest("https://qt.gtimg.cn/q=sh501005", "GBK");
        System.out.println(response);
        String code = response.substring(2, 10);
        String[] params = response.substring(12, response.length() - 2).split("~");
        System.out.println(Arrays.toString(params));
        System.out.println(params.length);
//        StockQuote stock = new StockQuote(
//                params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8],
//                params[9], params[10], params[11], params[12], params[13], params[14], params[15], params[16], params[17], params[18], params[19],
//                params[20], params[21], params[22], params[23], params[24], params[25], params[26], params[27], params[28], params[29],
//                params[30], params[31], params[32], params[33], params[34], params[35], params[36], params[37], params[38], params[39],
//                params[40], params[41], params[42], params[43], params[44], params[45], params[46], params[47], params[48]
//        );
//        System.out.println(stock);
    }

    @Test
    public void Test02() {
        String response = HttpClient.sendGetRequest("https://data.gtimg.cn/flashdata/hushen/weekly/sh600001.js");
//        System.out.println(response);
        String[] formData = response.substring(16, response.length() - 5).split("\\\\n\\\\");
//        System.out.println(Arrays.toString(formData));
        StockPriceSeries[] series = new StockPriceSeries[formData.length];

        int idx = 0;
        for (String row: formData) {
            String[] rowData = row.split(" ");
            System.out.println(Arrays.toString(rowData));
            series[idx++] = new StockPriceSeries(rowData[0], rowData[1], rowData[2], rowData[3], rowData[4], rowData[5]);
        }
        System.out.println(formData.length);
        System.out.println(Arrays.toString(series));
    }

    @Test
    public void test03() {
        List<TransactionSecurity> res = marketService.queryByType("STOCK", "SH", "60000");
        System.out.println(res);
        System.out.println(marketService.queryBySubtype("A", "SZ", "银行"));
    }

    @Test
    public void test04() {
        System.out.println("添加消息" + LocalDateTime.now());
        mqMessageService.addMessage("buy", "marlon1475", "sh600001/6.53/100", String.valueOf(LocalDateTime.now()));
        System.out.println("添加成功" + LocalDateTime.now());
    }

    @Test
    public void test05() throws UnsupportedEncodingException {
        List<TransactionMarket> res = marketService.getQuantMarketByCode("601800");
        System.out.println(res);
    }
}
