package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionBrokerageAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionBrokerageAccountMapper {
    List<TransactionBrokerageAccount> selectBrokerageAccountByUserId(@Param("uid") Integer uid);

    BigDecimal selectBrokerageBalance(@Param("uid") Integer uid, @Param("bid") Integer bid);

    int updateBrokerageBalance(@Param("uid") Integer uid, @Param("bid") Integer bid, @Param("balance") BigDecimal balance);

    int insertBrokerageAccount(TransactionBrokerageAccount brokerageAccount);
}
