package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionMarket;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionMarketMapper {
    List<TransactionMarket> selectMarketByCode(@Param("code") String code);

    List<TransactionMarket> selectMarketByCodeAndDate(@Param("code") String code, @Param("startDate") String startDate, @Param("endDate") String endDate);

    int insertMarket(TransactionMarket market);
}
