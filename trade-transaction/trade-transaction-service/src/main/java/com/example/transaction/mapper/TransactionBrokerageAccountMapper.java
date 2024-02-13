package com.example.transaction.mapper;

import com.example.transaction.model.dto.TransactionBrokerageAccountDTO;
import com.example.transaction.model.po.TransactionBrokerageAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionBrokerageAccountMapper {
    List<TransactionBrokerageAccountDTO> selectBrokerageAccountByUserId(@Param("uid") String uid);

    BigDecimal selectBrokerageBalance(@Param("uid") String uid, @Param("bid") Integer bid);

    int updateBrokerageBalance(@Param("uid") String uid, @Param("bid") Integer bid, @Param("balance") BigDecimal balance);

    int insertBrokerageAccount(TransactionBrokerageAccount brokerageAccount);
}
