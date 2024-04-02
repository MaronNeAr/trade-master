package com.example.transaction.mapper;

import com.example.transaction.model.po.TransactionMarket;
import com.example.transaction.model.vo.QuantMarketVo;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionMarketMapper {
    List<TransactionMarket> selectMarketByCode(@Param("code") String code);

    List<QuantMarketVo> selectQuantMarketByCode(@Param("code") String code);

    List<TransactionMarket> selectMarketByCodeAndDate(@Param("code") String code, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<QuantMarketVo> selectQuantMarketByCodeAndDate(@Param("code") String code, @Param("startDate") String startDate, @Param("endDate") String endDate);

    int insertMarket(TransactionMarket market);
}
