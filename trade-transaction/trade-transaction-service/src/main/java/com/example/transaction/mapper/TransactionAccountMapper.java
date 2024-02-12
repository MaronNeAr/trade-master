package com.example.transaction.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface TransactionAccountMapper {
    BigDecimal selectBalanceByUserId(@Param("uid") String uid);

    int selectDefaultBrokerageByUserId(@Param("uid") String uid);

    int updateBalanceByUserId(@Param("uid") String uid, @Param("balance") BigDecimal balance);
}
