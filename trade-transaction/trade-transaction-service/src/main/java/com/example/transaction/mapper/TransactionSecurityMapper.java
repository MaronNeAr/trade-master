package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionSecurity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionSecurityMapper {
    List<TransactionSecurity> selectSecurityByTypeAndExchange(@Param("type") String type, @Param("exchange") String exchange, @Param("keyword") String keyword);

    List<TransactionSecurity> selectSecurityBySubtypeAndExchange(@Param("subtype")String subtype, @Param("exchange") String exchange, @Param("keyword") String keyword);
}
