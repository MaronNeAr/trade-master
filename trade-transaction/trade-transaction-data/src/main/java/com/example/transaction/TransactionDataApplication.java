package com.example.transaction;

//import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableSwagger2Doc
@SpringBootApplication(scanBasePackages = "com.example")
@EnableScheduling
@EnableFeignClients(basePackages={"com.example.transaction.service.feignclient"})
public class TransactionDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionDataApplication.class, args);
    }

}
