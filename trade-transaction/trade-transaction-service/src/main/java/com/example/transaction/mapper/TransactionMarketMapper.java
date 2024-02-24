package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionMarket;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionMarketMapper {
    List<TransactionMarket> selectMarketByCode(@Param("code") String code);

    int insertMarket(TransactionMarket market);
}
