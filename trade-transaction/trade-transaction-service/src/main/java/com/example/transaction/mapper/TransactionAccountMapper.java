package com.example.transaction.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface TransactionAccountMapper {
    BigDecimal selectBalanceByUserId(@Param("uid") Integer uid);

    int selectDefaultBrokerageByUserId(@Param("uid") Integer uid);

    int updateBalanceByUserId(@Param("uid") Integer uid, @Param("balance") BigDecimal balance);
}
